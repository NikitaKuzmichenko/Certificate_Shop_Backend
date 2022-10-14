# Gift certificates shop
### Application description
This backend application is designed to create, update, delete, purchase gift certificates.<br>
 * **Key elements**
    - Spring boot
    - Lombock
    - Postgresql database
    - Spring data jpa
    - Spring Security
    - Rest assured
    - Junit
    - Mockito
    - Swagger
    - Pagination
    - Localization: EN, RU

The user's roles and actions available to them are described in the table below.
      
Command | GUEST | USER | ADMIN 
---------|-------|--------|------
Change language| * | * | * |
Login| * | * | * |
Sin up| * |   |   |
Refresh access token| * | * | * |
Cereate certificate|   |   | * |
Path certificate|   |   | * |
Put certificate|   |   | * |
Delete certificate|   |   | * |
Get certificate| * | * | * |
Get all certificates| * | * | * |
Cereate tag|   |   | * |
Delete tag|   |   | * |
Get tag|  | * | * |
Get popolar tag|  | * | * |
Get all tags|  | * | * |
Cereate order|   | * | * |
Get order|  | * | * |
Get user's orders|  | * | * |
