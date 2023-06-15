# USCDoorDrink_FrontEnd

## Environment
   This application is built and tested with Android API Level 32, on emulator Nexus 5X in 
   Android Studio 2021.1.1 Patch 1. We strongly recommend running this application under the same 
   environment for best performance.

## Updates
### April 24 (For Assignment 2.5)
#### 1. Manage stores and menus
   An additional feature for sellers to manage their stores in their user profile page. 
   After clicking the button, sellers will be able to edit their stores’ names, address, and menus.
#### 2. Improve Location Performance
   An additional feature to animate the camera to the user’s current position when the map is launched. 
   Users can also tap “My location button” to update their current position and surrounding stores.
#### 3. Improve Text box when editing store
   An additional feature that makes text boxes clearing their hints when being tapped. 
#### 4. Delete Drink
   An additional feature that enables sellers to delete drinks from their menu. 
#### 5. Remember Me
   After clicking the checkbox named “remember me” on the login page, user do not
   need to enter password and nickname again to login, users will log in
   automatically.
#### 6. UI Improvement
   Adding USCDoorDrink Icon and Slogan, background, and improve xml design
#### 7. Show Password
   An additional feature that to display or hide password when clicking the eye
   button on the login page.
#### 8. Improve Recommendation System
   After clicking on the drinks in the recommendation page, an additional feature to switch to the corresponding store’s menu.
#### 9. Modify Profile
   An additional feature to modify the user's password and contact info in profile page.
#### 10. About Us
   Adding a page of about us, displaying information about this software and developers.
#### 11. AddStore
   Newly registered sellers will now be automatically prompted to add a store after their first login.

## Guide
#### 1. Before launching the app:
   This app only shows the stores that are not farther than 2 miles away from the device's location.
   We already prepared 5 test stores for you near USC. You can change your emulator's location to a 
   place near USC to see them. The app also works in other places, but you will have to add stores 
   by yourself. The stores can be added and managed when logging in as a seller.
   
#### 2. Launching the app:
   Please give permission of precise location when prompted by the app.

#### 3. Map
   a. A Map will be displayed once the app is launched. You can click on the floating button on 
      bottom left of the screen to access more functionalities (eg: login, view cart, view order,
      manage order, see recommendations, see profile), but most of them will need logging in!

   b. The map will automatically locate your position when launched. To update your location, tap the button on the top right.
   
   c. Store around you will appear as markers on the map. Markers might take a few seconds to load. 
      You can tap on them to see the route and the time needed for the deliveryman to reach your place.
      You can switch the mode of transportation, BUT DO NOTE that if you want to show bicycling, please tap the right TIP of the bicycling chip.
   
   d. If you do not want to order at the current store, you can tap another marker or tap the floating 
      button on the bottom left. If you do want to order at the current store, click "Order at this 
      store". This requires login as well.
   
#### 4. Login/Register
   a. At anytime on the map, you can tap the floating button on the bottom left, and select the one 
   with a cell phone icon to login.

   b. After successful registration, you will be prompted back to the login page. If you chose to
   register as a seller, you will be prompted to add a store for yourself after your first login.

   c. When entering the store address, please select one from the drop-down menu.

#### 5. User Profile
   At anytime on the map, you can tap the floating button on bottom left, and select the button with ID card icon
   to go to the User Profile page. Please note that this requires logging in.

   You can also view your spending history in a daily, weekly, and monthly basis by clicking the View button.

#### 6. Current order status 
   Customers can view their current order by tap the floating button on bottom left, and select the button with a glass icon.
   You can click all the orders to see their details.

#### 7. Recommendation
   To get today's recommended drink, tap the floating button on bottom left, and select the button with a thumb icon.

#### 8. Cart
   To view your cart, tap the floating button on bottom left, and select the button with a cart icon.

#### 9. Order management:
    a. For sellers, when you have a new order, you can pull the page from the top and then check the notification center.
   You will receive a new notification reminding that you have a new order. You can click the notification, then click
   one of the orders on the view order page, and click the management order button on the order info windo. Here you can
   change the status of this order. When you hit apply, order status will be changed. But as a reminder, only customer
   can change the order to the 'complete' status.

   b. For customers, when your order has a status change, you will receive information in the notification center. You can set order status
   to 'complete' and non longer receive any information about this order by clicking the notification, and then click the current order,
   select the 'complete order' button on the order info window.
   
#### 10. Store management:
   Sellers can manage their stores in their profile page. You can edit your store's name, address, menus and discounts.

#### 11. Caffeine Alert
   If user tries to order more than five drinks on a single day, user will be prompt with an alert window to alert excessive caffeine amount intake.

#### 12. Menu
    Customers can add drink from menu to their carts by clicking the menu button. They can also see drink ingredients details by clicking the drink name.