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
import com.group34.cooked.fragments.NewRecipeInformationFragment
import com.group34.cooked.fragments.NewRecipeIngredientsFragment
import com.group34.cooked.fragments.NewRecipeInstructionsFragment
import com.group34.cooked.fragments.NewRecipePublishFragment
import com.group34.cooked.models.RecipeCreationStatus

class NewRecipeActivity : AppCompatActivity() {
    private val tabTitles = arrayOf("Information", "Ingredients", "Instructions", "Publish")

    private lateinit var newRecipeViewModel: NewRecipeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_create)

        newRecipeViewModel = ViewModelProvider(this)[NewRecipeViewModel::class.java]

        val viewpager = findViewById<ViewPager2>(R.id.new_recipe_view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.new_recipe_tabs)

        val fragments = ArrayList<Fragment>().apply {
            add(NewRecipeInformationFragment())
            add(NewRecipeIngredientsFragment())
            add(NewRecipeInstructionsFragment())
            add(NewRecipePublishFragment())
        }

        viewpager.adapter =  MyFragmentStateAdapter(this, fragments)

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
                if (newRecipeViewModel.recipe.value!!.isRecipeValid()) {
                    publish()
                    Toast.makeText(this, "Recipe Published!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }

    private fun publish() {
        newRecipeViewModel.setStatus(RecipeCreationStatus.PUBLISHED.value)

        // Save the recipe to Firestore
        newRecipeViewModel
            .saveRecipeToFireStore()
            .addOnSuccessListener { documentReference ->
                Log.d("NewRecipe", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("NewRecipe", "Error adding document", e)
            }

        finish()
    }
}