package club.umdgdc.pages

import androidx.compose.runtime.Composable
import com.varabyte.kobweb.compose.css.StyleVariable
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Color
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.data.add
import com.varabyte.kobweb.core.init.InitRoute
import com.varabyte.kobweb.core.init.InitRouteContext
import com.varabyte.kobweb.core.layout.Layout
import com.varabyte.kobweb.core.rememberPageContext
import com.varabyte.kobweb.silk.components.forms.Button
import com.varabyte.kobweb.silk.components.navigation.Link
import com.varabyte.kobweb.silk.components.text.SpanText
import com.varabyte.kobweb.silk.style.CssStyle
import com.varabyte.kobweb.silk.style.base
import com.varabyte.kobweb.silk.style.breakpoint.Breakpoint
import com.varabyte.kobweb.silk.style.breakpoint.displayIfAtLeast
import com.varabyte.kobweb.silk.style.toAttrs
import com.varabyte.kobweb.silk.style.toModifier
import com.varabyte.kobweb.silk.theme.colors.ColorMode
import com.varabyte.kobweb.silk.theme.colors.ColorPalettes
import org.jetbrains.compose.web.css.cssRem
import org.jetbrains.compose.web.css.fr
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.vh
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import club.umdgdc.HeadlineTextStyle
import club.umdgdc.SubheadlineTextStyle
import club.umdgdc.components.layouts.PageLayoutData
import club.umdgdc.toSitePalette

// Container that has a tagline and grid on desktop, and just the tagline on mobile
val HeroContainerStyle = CssStyle { //defines the style variable name
    base { Modifier.fillMaxWidth().gap(2.cssRem) } // base applies to all screen sizes, gap is seperate from margin and padding. 2 modifiers here
    Breakpoint.MD { Modifier.margin { top(20.vh) } } // MD(medium) screens or larger gets 20% margine on the top
}

// A demo grid that appears on the homepage because it looks good
val HomeGridStyle = CssStyle.base { //base here defines this function to only be used for modifiers that apply universally, no Breakpoint.MD or hover bs
    Modifier
        .gap(0.5.cssRem)
        .width(70.cssRem)
        .height(18.cssRem)
}

// css styling for the color of each box
private val GridCellColorVar by StyleVariable<Color>() //by i think is simmilar to @export in godot, stylevariable<color> is kind of like the custom css export variable type?
val HomeGridCellStyle = CssStyle.base { // the export var's parameters, i think this can be changed for each item
    Modifier
        .backgroundColor(GridCellColorVar.value())
        .boxShadow(blurRadius = 0.6.cssRem, color = GridCellColorVar.value())
        .borderRadius(1.cssRem)
}

@Composable //This is how you make a box
private fun GridCell(color: Color, row: Int, column: Int, width: Int? = null, height: Int? = null) {
    Div( //makes a html div
        HomeGridCellStyle.toModifier() //uses the css properties made in defined right before this area
            .setVariable(GridCellColorVar, color) //setvariable turns GridCellColorVar into the color variable in the second parameter
            .gridItem(row, column, width, height) //sets the size and position, assuming this item is on a grid
            .toAttrs() //this is like the build thing in the builder i think, well it just turns everything into html elements
    )
}


@InitRoute //makes sure this is run before the page is rendered
fun initHomePage(ctx: InitRouteContext) {
    ctx.data.add(PageLayoutData("Home")) //addes data to the page, this allows the tab to be named
}

@Page //marks this as a new html page
@Layout(".components.layouts.PageLayout") // okay so this links to the .kt file it is talking about. it sets up a baseplate for the website. we can add this @layout to other kotlin @pages to setup the default cobweb, main area, and footer for every website using this annotation
@Composable
fun HomePage() {
    Row(HeroContainerStyle.toModifier()) { //row which uses the HeroContainerStyle css style made above, this row is not the header, header is dealth with ^ layout. this splits the text and the grids
        Box { //this area holds all of the text and buttons
            val sitePalette = ColorMode.current.toSitePalette() //just a variable that holds the palette this val changes depending on light or dark mode

            Column(Modifier.gap(2.cssRem)) { //splits the text bodies with the 2 rem gap
                Div(HeadlineTextStyle.toAttrs()) { //headlinetextstyle makes it bigggg
                    SpanText( //main text
                        "Use this template as your starting point for ", Modifier.color(
                            when (ColorMode.current) {
                                ColorMode.LIGHT -> Colors.Black
                                ColorMode.DARK -> Colors.White
                            }
                        )
                    )
                    SpanText( // yellow "kobweb" text
                        "Kobweb",
                        Modifier
                            .color(sitePalette.brand.accent)
                            // Use a shadow so this light-colored word is more visible in light mode
                            .textShadow(0.px, 0.px, blurRadius = 0.5.cssRem, color = Colors.Gray)
                    )
                }

                Div(SubheadlineTextStyle.toAttrs()) { //subheadlinetextstyle makes this div stuff smalllll
                    SpanText("You can read the ")
                    Link("/about", "About") // so theres a resources\markdown\about.md this is markdown file that kobweb automatically converts into html
                    SpanText(" page for more information.")
                }

                val ctx = rememberPageContext() //you need this to access the pagecontext var, its an object with data on the page, including the router object which is used down below here
                Button(onClick = {
                    // Change this click handler with your call-to-action behavior
                    // here. Link to an order page? Open a calendar UI? Play a movie?
                    // Up to you!
                    ctx.router.tryRoutingTo("/about")
                }, colorPalette = ColorPalettes.Blue) { //button styling ends here on this light with that parentheses there, idk the formating confused me for a bit
                    Text("This could be your CTA")
                }
            }
        }

        Div(// here are those decorative boxes :D
            HomeGridStyle //the styling for what is going to hold all the little boxes
            .toModifier()
            .displayIfAtLeast(Breakpoint.MD) //dont show the boxes on mobile devices :(
            .grid {
                rows { repeat(3) { size(1.fr) } }
                columns { repeat(5) { size(1.fr) } }
            } //makes this a grid to hold baby boxes
            .toAttrs() //turns this into html code
        ) {
            val sitePalette = ColorMode.current.toSitePalette() //this line was technically called earlier, its called again here as it was called once in the other text area under box
            GridCell(sitePalette.brand.primary, 1, 1, 2, 2) //and here they all are generated, handcrafted one might say
            GridCell(ColorPalettes.Monochrome._600, 1, 3)
            GridCell(ColorPalettes.Monochrome._100, 1, 4, width = 2)
            GridCell(sitePalette.brand.accent, 2, 3, width = 2)
            GridCell(ColorPalettes.Monochrome._300, 2, 5)
            GridCell(ColorPalettes.Monochrome._800, 3, 1, width = 5)
        }
    }
}
