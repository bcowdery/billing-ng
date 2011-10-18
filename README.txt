Billing NG
An open source, next-generation enterprise billing solution.

Brian Cowdery (bcowdery@pointyspoon.com)
http://pointyspoon.com
------------------------------------------------------------

For now this is a sandbox project that contains billing and billing-related
code developed following strict development and testing policies. It is not
likely that this project will ever become a full-fledged billing system as
it is developed in my spare time, usually to satisfy my own curiosity.


Database setup:
------------------------------------------------------------
mysql -u root

mysql> create user billing identified by 'password';
mysql> grant usage on *.* to billing@localhost;
mysql> grant all privileges on billing.* to billing@localhost identified by 'password';
