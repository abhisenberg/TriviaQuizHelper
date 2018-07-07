# QuizHelper
With the increase in number of online trivia quiz games (HQ-Trivia, Loco etc) which give-out real money to the winners,
players all around the world have been thinking of creative ways to cheat in the game (from doing conference calls with friends
during the contest, to speaking out the question to Alexa/Google Assistant/Siri to get some hint for the question).

This app is the quickest and most accurate solution to get answers to the quiz questions and win real money.

### How?
Let's see a simple demonstration of this app, and how it will make you win money in Trivia quiz contests:

1. Open the app before the contest starts, this is the landing page for you:

![qh_1](https://user-images.githubusercontent.com/29260302/42408163-cf7c7ac4-81e5-11e8-858c-d0a0b4ea265a.png)

2. Press the 'start' button, this button will create a screen-overlay window (permission to draw over other apps should be
provided by the user) at the bottom of the screen.

![qh_2_new](https://user-images.githubusercontent.com/29260302/42408187-22ebca34-81e6-11e8-9fb0-972b81b6b090.png)

This window has 3 elements:
  * Close button (denoted by red cross)
  * Capture button
  * Question view panel (the white empty space)

We shall see later the use and significance of all of these components.

3. Open the trivia quiz app (in this example, Loco) and let the contest start, when the question comes up,
the screen will look like this:

![qh_3](https://user-images.githubusercontent.com/29260302/42408169-d3b5a994-81e5-11e8-9430-0a79df5a94a9.png)

Since this app's window is screen-overlay, it will always stay on screen, on the top of view hierarchy. 

4. Press the 'capture' button at the left-bottom of your screen:

![qh_button](https://user-images.githubusercontent.com/29260302/42408174-de2703fa-81e5-11e8-8827-d1912d702c5a.png)

5. Within 1-2 seconds of pressing it, a floating window will pop-up on the top of the screen, which displays the Google
search result of the question. This search is the same as manually searching for the question on Google, but x1000 times faster.

![qh_4](https://user-images.githubusercontent.com/29260302/42408170-d6da34be-81e5-11e8-84a0-128236645d92.png)

**Important points to notice:**
  * The popup window's size is designed in such a way that the answer-options are not blocked, otherwise the user would not have
  have been able to press the correct answer's option (and the app would have been useless).
  * Only the question's view is blocked by the popup, but for that I have added the empty white space at the bottom, which
  displays the entire question text once the popup window comes up.
  * Once you have seen the Google search result on the popup window and pressed that correct option, **press the cross button**
  to close the popup window, otherwise you won't be able to see the next question.

Let's see another example on another trivia quiz app, this time HQ-Trivia:

1. Open this app, press the start button, resulting in the bottom screen-overlay panel. Then open the quiz app,
let the question come up, like this:

![qh_5](https://user-images.githubusercontent.com/29260302/42408172-d95faaac-81e5-11e8-8455-1a0249f3ca06.png)

2. Press the capture button at the bottom-left, and the Google search result in displayed on the popup, from which 
the correct answer can be deduced.

![qh_7](https://user-images.githubusercontent.com/29260302/42408173-dbf77a24-81e5-11e8-80b0-8f89de2a2ae5.png)
