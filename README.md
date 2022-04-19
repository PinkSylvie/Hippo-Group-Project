# EDM-Group-Project
CodePath GroupProject
Unit 8: Group Milestone - Hippo
===


# Hippo

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)

## Overview
### Description
Our app is an organization app that helps the user to organize what they need to do, while the giving the user the ability to make timed to-do lists and take notes.

### App Evaluation
- **Category:** Productivity
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on a computer, juist like other similar apps. Functionality wouldn't be limited to mobile devices, however mobile version would be more comfortable to use.
- **Story:** It helps the user to organize what they need to do, while giving the user the ability to make timed to-do lists and take notes.
- **Market:** Any individual could choose to use this app.
- **Habit:** This app could be used as often or unoften as the user needs.
- **Scope:** The app would start as a simple organization app but could grow in scope to help the user keep habits, with more development a calendar could be implemented that connects the users lists to it and displays them.

## Product Spec
### 1. User Stories (Required and Optional)

**Completed Stories**

- [X] Setup Backend for User purposes
- [X] Setup User Login
- [X] Setup User Signin
- [X] Setup Task Compose
- [X] Setup Task View
- [X] Start Calendar View

**Required Must-have Stories**

* User has the ability to make lists with reminder and dates.
* The user would be able to create notes.
* The user would be able to see all their notes and edit them on a separate view.

**Optional Nice-to-have Stories**

* Add a calendar feature that would be connected to the to-do lists
* Add location of a to-do
* Add visual themes for the user to pick and change the visual interface 

**1st Sprint GIF**

Here's a walkthrough of implemented user stories:

<img src='https://github.com/PinkSylvie/EDM-Group-Project/blob/main/Hippo%201st%20Sprint.gif' width='' alt='1st Sprint Walkthrough' />

**2nd Sprint GIF**

Here's a walkthrough of implemented user stories:

<img src='https://github.com/PinkSylvie/EDM-Group-Project/blob/main/Hippo%202nd%20Sprint.gif' width='' alt='2nd Sprint Walkthrough' />



### 2. Screen Archetypesm

* List of to-do lists Screen - The user would be able to see their created to-do lists and create new ones.
* To-do list Screen - User can view a specific to-do list and add or erase elements from it.
* Note list screen - Here the user will be able to see a list of the titles of their created notes.
* Note creation screen - A separate screen in which the user is able to create a new note.
* Note View and Edit Screen - User is able to view the full extent of a note of their choosing and edit it.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Lists
* Notes

Optional:
* Create Lists
* Create Notes

**Flow Navigation** (Screen to Screen)
* List of To-do lists Screen -> View a selected To-do list or Create/Edit a To-do list
* Lists of Notes -> View a selected note or Create/Edit a Note

## Wireframes
![Wireframe](/WireframeWithWires.png)

## Schema 
### Models
#### Task

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user task (default field) |
   | user          | Pointer to User| user whose task belongs to |
   | tittle        | String   | tittle of the task |
   | description   | String   | description of the task|
   | dueTime       | DateTime | time and date of when the task must be completed |
   | reminderTime  | DateTime | time and date when the task will be reminded |
   | isDone        | Boolean  | shows if the task is done or not |
   | attachment    | File     | file that will be propmted with the task  |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |

### Networking
#### List of network requests by screen
   - Home Feed Screen
      - (Read/GET) Query all tasks where user is author
         ```swift
         let query = PFQuery(className:"Task")
         query.whereKey("author", equalTo: currentUser)
         query.order(byDescending: "createdAt")
         query.findObjectsInBackground { (tasks: [PFObject]?, error: Error?) in
            if let error = error { 
               print(error.localizedDescription)
            } else if let tasks = tasks {
               print("Successfully retrieved \(task.overdue) tasks.")
           // TODO: Do something with task|move into calendar activity...
            }
         }
         ```
   - Create task Screen 
      - (Create/POST) Create a new task object
   - Calendar | View tasks Screen
      - (Read/GET) Query logged in user object
      - (Read/GET) Query tasks objects for logged user
      - (Delete) Delete existing task
      - (Update/PUT) Update existing tasks
