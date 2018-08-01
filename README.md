# Selfie Challenge


Selfie Challenge is Android app in which enables users to participate in challenges of taking selfies through a certain theme, being the photos sent to these challenges and evaluated by image evaluation APIs such as Google Cloud Vision API.
<p align="center">
  <img src="https://i.imgur.com/k2JjarV.jpg" alt="Size Limit example"
       width="60%" height="60%">
</p>

It is in this view that the challenges created by other users are presented and where they can visualize the selfies / photographs that were sent by them for each challenge.
In this view the user is also allowed to participate in a challenge by touching the "Enter Challenge" button and after taking the picture this is followed for evaluation to verify that it is within the parameters of the challenge and then it is automatically accepted and synchronized for all users .



## Leaderboard


<p align="center">
  <img src="https://i.imgur.com/ehwI552.png" alt="Size Limit example"
       width="50%" height="50%">
</p>

The challenges created by other users are presented and In this view a list of user ratings is displayed, sorted by the number of points each has.
Points are earned by creating and completing challenges successfully.

## Night Mode

<p align="center">
  <img src="https://i.imgur.com/lKvrH1w.png" alt="Size Limit example"
       width="50%" height="50%">
</p>

Using the context provided by the APIs we used, we made the decision to create a nightly version of the application so that when it is dark, the user's eyes do not suffer from the light colors of the application, thus replaced by a dark color palette.
This mode is activated from 8 pm until 6 am.

## Creating public challenges

<p align="center">
  <img src="https://i.imgur.com/SglMdUY.png" alt="Size Limit example"
       width="50%" height="50%">
</p>

In this view, a simple form is presented where any user can create challenges based on already defined keywords and where he can provide a description for them.
After the user touches "Create" the challenge is created in Firebase and synchronized with all users of the application.

## APIs Used

* Facebook
* Firebase
* AwarenessAPI
* Google Places Web Service
* Google Cloud Vision API
