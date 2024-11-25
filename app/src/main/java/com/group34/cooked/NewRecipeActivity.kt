package com.group34.cooked

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.google.firebase.auth.FirebaseAuth
import com.group34.cooked.fragments.NewRecipeInformationFragment
import com.group34.cooked.fragments.NewRecipeIngredientsFragment
import com.group34.cooked.fragments.NewRecipeInstructionsFragment
import com.group34.cooked.fragments.NewRecipePublishFragment
import com.group34.cooked.models.RecipeCreationStatus

class NewRecipeActivity : AppCompatActivity() {
    private val tabTitles = arrayOf("Information", "Ingredients", "Instructions", "Publish")

    private lateinit var newRecipeViewModel: NewRecipeViewModel
    private lateinit var fragmentStateAdapter: MyFragmentStateAdapter
    private lateinit var viewpager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_create)

        newRecipeViewModel = ViewModelProvider(this)[NewRecipeViewModel::class.java]
        newRecipeViewModel.setCreatorId(FirebaseAuth.getInstance().currentUser!!.uid)

        viewpager = findViewById(R.id.new_recipe_view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.new_recipe_tabs)

        val fragments = ArrayList<Fragment>().apply {
            add(NewRecipeInformationFragment())
            add(NewRecipeIngredientsFragment())
            add(NewRecipeInstructionsFragment())
            add(NewRecipePublishFragment())
        }
        fragmentStateAdapter = MyFragmentStateAdapter(this, fragments)
        viewpager.adapter = fragmentStateAdapter

        // Set the title of each tab
        val tabConfig = TabConfigurationStrategy { tab, position ->
            tab.text = tabTitles[position]
        }

        // Synchronizes viewPager and tabLayout to change the position when a tab gets clicked or swiped
        val tabLayoutMediator = TabLayoutMediator(tabLayout, viewpager, tabConfig)
        tabLayoutMediator.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                updateToolBar(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    // Update the toolbar based on the tab index
    // Only the publish tab has a menu item
    private fun updateToolBar(tabIndex: Int) {
        val toolbar = findViewById<Toolbar>(R.id.new_recipe_toolbar)
        toolbar.menu.clear()

        // Publish tab - show publish button in menu
        if (tabIndex == 3) {
            toolbar.inflateMenu(R.menu.menu_publish)

            // Set the color of the menu item text to white
            val publishItem = toolbar.menu.getItem(0)
            val spanString = SpannableString(publishItem.title)
            spanString.setSpan(ForegroundColorSpan(Color.WHITE), 0, spanString.length, 0)
            publishItem.title = spanString

            // Handle click event of publish
            publishItem.setOnMenuItemClickListener {
                handlePublish()
                true
            }
        }
    }

    // Handle the button functionality of publish
    // Publish a recipe if it is valid
    // Otherwise, navigate to the first invalid tab
    private fun handlePublish() {
        val recipe = newRecipeViewModel.recipe.value!!

        // Only publish the recipe if it is valid
        if (recipe.isRecipeValid()) {
            publish()
            Toast.makeText(this, "Recipe Published!", Toast.LENGTH_SHORT).show()

        // Invalid recipes will show a toast and navigate to the first invalid tab
        } else {
            val infoFragment = fragmentStateAdapter.getFragment(0) as? NewRecipeInformationFragment
            val isInfoValid = infoFragment?.areInputsValid() ?: true
            val errorMsg: String

            if (!isInfoValid) {
                viewpager.currentItem = 0
                errorMsg = "Please fill in all the fields"
            }
            else if (recipe.ingredients.isEmpty()) {
                viewpager.currentItem = 1
                errorMsg = "Please add ingredients"
            }
            else if (recipe.instructions.isEmpty()) {
                viewpager.currentItem = 2
                errorMsg = "Please add instructions"
            } else {
                errorMsg = "ERROR: Recipe can not be published"
            }

            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }

    // Publish a recipe
    private fun publish() {
        newRecipeViewModel
            .saveRecipeToFireStore(RecipeCreationStatus.PUBLISHED)
            .addOnSuccessListener { documentReference ->
                Log.d("NewRecipe", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("NewRecipe", "Error adding document", e)
                Toast.makeText(this, "Error publishing recipe", Toast.LENGTH_SHORT).show()
            }

        finish()
    }

}