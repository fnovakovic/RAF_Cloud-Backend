# RAF_Cloud-Backend
Advance Web Programming - Final project

Web Application that represents an imitation of a cloud provider. The application should create the impression that the user is able
to create and control the state of the physical machine and related
resources.

Here is the functionality of this project:

User management: The project will use already implemented user management functionality, including permissions that determine what users can do.

Machine Management: Machines are the basic entities in the system and have attributes such as ID, status (STOPPED or RUNNING), owner (user who created the machine), and activity status (soft-delete). Users can perform various actions on machines, such as START, STOP, RESTART, CREATE and DESTROY. Each of these actions is accompanied by a corresponding permission.

Scheduling operations: Users can schedule operations such as START, STOP and RESTART on machines for a specific time. The system will automatically attempt to perform the operation at the scheduled time if the appropriate conditions are met. Errors when executing operations are logged in the ErrorMessage table.

Frontend: Frontend applications will allow users to search machines based on various parameters as well as create new machines. It will also display a history of errors that occurred while executing scheduled operations.

Permissions: Every action on machines will be controlled by permissions. Users must have appropriate permissions to perform a specific action on machines. Without the appropriate permissions, users will not be able to access or perform certain operations.



