# wepa-projekti
My project is a website in which users can:
* Create an account
* Create multiple choice questions  
* Browse and answer own and others' questions
* Gain score  

An admin can delete questions from the answer page.  

**My webpage**: https://guarded-ridge-13882.herokuapp.com/login  
**Test account**: user user  
**Travis**: https://travis-ci.org/woltsu/wepa-projekti  
**Api** that the bot uses to get random questions: https://opentdb.com/api_config.php  

The project is programmed with Java using Spring framework. The database in Heroku uses PostgreSQL. Tests use Fluentlenium and Selenium. If all tests pass when the project is pushed to Github, Travis automatically pushes the project into Heroku. The website utilizes many Bootstrap components and rescales quite nicely to other platforms as well. Accounts' passwords are protected using BCrypt and the website can only be used with https connection.
