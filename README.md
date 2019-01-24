# PhoneBook

PhoneBook is an application developed by Jan Lastovicka as a task from Micro Focus. 

This application allows managing contacts:
* open existing phone book
* create new phone book
* create new contact
* remove existing contact

## Design consideration

### persistence
I chose XML file as a persistence storage and XStream library as a persistence framework.
**Why**: if we consider the application to be used on a daily basis, it makes sense to have
contacts in some readable and easily consumable format, which XML surely is. We can export 
the contact list to any other format with help of XSLT transformation for instance. Of
edit contacts directly. This solution seems to me better than using some heavy DB framework
like JPA etc.

### backend
I chose **DDD** (Domain Driven Design) approach since this architecture affers clean
separation of concerns like repositoris, entities, value classes etc. I like this
architecture because it also forces you to adhere some rules that should be kept
and allows the code to be more organized and newcomer can easily start working on
the project.

### frontend
I chose JavaFX graphical library that is part of Java language.
**Why**: Even though I am a big a fan of command line tools, if I consider this application to be
used frequently and quickly, then it makes me more sense to have some simple UI interface,
to have simple desktop application that I can quickly start just be clicking on its icon
on my desktop and quickly find what I am currently looking for. 


## Architecture

![Architecture](images/architecture.jpg)

