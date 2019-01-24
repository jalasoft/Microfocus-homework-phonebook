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

**Contact** is an aggregate entity. It represents the smallest persistence unit, having all 
information in it to describe a contact - name and phone numbers. It is an entity that is
identified by its id (which is not persistence id).


**ContactId** it is a unique identifier of the Contact entity. It is a value object.

**ContactName** it is a value class that serves as a name of a contact, like John Doe etc. 
It consists of two parts - first name and last name. From DDD pespecive, this class is a value
class that describes the owning entity - Contact.

**PhoneNumber** similarly like ContactName, this class is a value class that is owned by
Contact, storing one phone number that must conform to a pattern +XXX XXX XXXXXX where X is
a digit. An owning entity Contact contains one or more phone numbers.

**ContactDescriptor** describes a contact, it is a value class that can bu used as a sort
description of a contact without numbers.

**ContactsRepository** is a DDD concept that works with Contact aggregate. It represents a collection
of contacts no matter whether they are held in memory, or stored in some persistence storage.
It is just an interface for obtaining and storing contacts.

**PhoneBookService** is an application service that contains methods representing use cases
perfomed by the user. From DDD perspecive, this application service coordinates DDD objects
like repository, aggreagates etc. This class is an entry point to the application backedn from UI.
All the UI infrastructure accesses backend only via this class.

**XmlContactRepository** is an implementation of ContactsRepository that does IO operations
with XML file as a storage.


 