# Zappos
Android shopping application demo for zappos.com

### Requires Android version 5.0 or above.
(Since this is a demo app, backward compatibility is not implemented)

This app. uses well known libraries, [Retrofit](https://square.github.io/retrofit/) as HTTP client and [Glide](https://github.com/bumptech/glide) for image downloaiding and display. I have experimented with other Image loading libraries: [Picasso](http://square.github.io/picasso/), [Volly](https://github.com/google/volley) and [Fresco](https://github.com/facebook/fresco), but GLide seems to be performing best amongst all due to it's well optimized Cache mechanism. Since default type 'THUMBNAIL' images are too small, I have used type 'PAIR' images with '4x' resolution to enhance user experience. Used [databinding](https://developer.android.com/topic/libraries/data-binding/index.html) for efficient data loading and save number of lines of code. Designed with [Meterial design guideline](https://material.io/guidelines/) to provide elegant user interface. Detailed explaination about implementation has been provided inside source code.


## Demo (Click [here](screenshots) to see HD screenshots)
### 1. Search products
![Search products](https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/gifs/search.gif)
### 2. List all serached products
![List serached products](https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/gifs/grid_list_view.gif)
### 3.Product page
![Product page](https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/gifs/product_page.gif)
### 4. Add to cart animation
![Add to cart](https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/gifs/add_to_cart.gif)
### 5. Share product with others
![Share product](https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/gifs/share_product.gif)
### 6. Config changes
![Config changes](https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/gifs/config_chnages.gif)
### 7. Navigation drawer
<img src="https://raw.githubusercontent.com/arjunvekariyagithub/ILoveZappos/master/screenshots/navigation_drawer.png"
alt="Navigation drawer" width="300" height="540" border="10" /></a>
