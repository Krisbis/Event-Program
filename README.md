# Event-Program

Building small program as part of course. Assignment was to create event-creating/storing program, that can do basic CRUD- operations. I quickly got enthusiastic, and gave my program few additions.

Program is written in Java, and is operated in CLI using commands and flags. Currently, program can add, delete, search and list events. Created events are stored to "events.csv" file, which program automatically creates if file with that name is not present. User can also choose to set file path manually to users liking, using command " java MAIN config --set-file-path <filepath>". By doing so, properties.config file is created, and path is stored in environment variable, which in turn is saved in the created .config file. In this way user can have multiple CSV-storages for events, and creates possibility to exporting and importing event-lists. I also added hashing to main class Event, so it doesnt accept multiple occurences of same event in one csv- storage file.


Supported commands: list, add, delete, config

Options for list command:
--today
--before-date
--after-date
--between-dates
--category
--all

Options for add command:
[--date --category --description]

Options for delete command:
--date
--category
--description
--all
--file
--dry-run

Options for config command:
--set-file-path
--get-file-path
--reset-file-path
