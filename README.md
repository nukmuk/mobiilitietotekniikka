### HW1 description

### HW2 description

The essential parts for implementing navigation are a navigation host and controller. You can make a
button that takes you to another view in the app by calling the controller's navigate function when
clicking the button. Circular navigation can be prevented by using `popBackStack()` instead of
`navigate()`, or by specifying `popUpTo()` and `inclusive` in the navigate call