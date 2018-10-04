## Swipe Over Buttons Demo ##

In this demo, four radio buttons are registered to receive clicks. When clicked, the background color changes.

In addition, there's a secret left-to-right swipe action that causes the background to turn black. The swipe must go *over* the radio buttons and not vertically outside of them to work! The individual radio buttons get ``onClickListener``s and the application itself receives ``onTouchEvent``s to check for the correct swipe.

### layout view ###

This is what the application looks like in Android Studio's design view. There's a white frame indicating the ``LinearLayout``around the radio buttons. [The boundaries of the ``LinearLayout`` are computed after the application's view is "inflated"](https://stackoverflow.com/questions/21926644/get-height-and-width-of-a-layout-programmatically?rq=1). This is the point when the boundaries can be measured correctly. Then, it's just a matter of checking to make sure the user's swipe does not stray outside the vertical bounds of the layout.

### Thanks! ###

Thanks to [inky2010](https://openclipart.org/user-cliparts/inky2010?page=7) for making the flower images available in the public domain!