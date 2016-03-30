Integration Testing


UI Testing: Few other sample test conditions can be as follows:

Are the menu options generating the correct window?
Are the windows able to invoke the window under test?
For every window, identify the function calls for the window that the application should allow.
Identify all calls from the window to other features that the application should allow
Identify reversible calls: closing a called window should return to the calling window.
Identify irreversible calls: calling windows closes before called window appears.
Test the different ways of executing calls to another window e.g. – menus, buttons, keywords.


- Understand the architecture of your application.
- Identify the modules
- Understand what each module do
- Understand how the data is transferred from one module to another.
- Understand how the data is entered and received into the system ( entry point and exit point of the application)
- Segregate the application to suite your testing needs.
- Identify and create the test conditions
- Take one condition at a time and write down the test cases.

<<DCHQ Architecture>>
  - Postgres
  - Redis
  - Solr
  - RabbitMQ
  - Tomcat
  - LB

<<DCHQ Modules>>
  - Identity:
     Users:
       Create (annonymous & ENABLED, ROLE_TENANT_ADMIN)
         valid
         invalid: username, email, role, pass
       Read   (ROLE_USER for sharing)
       Update (SELF, ROLE_TENANT_ADMIN)
         invalid: dup username, email, role, pass
       Delete (ROLE_TENANT_ADMIN)
       Disabled (ROLE_TENANT_ADMIN)
       Change ROLE (ROLE_TENANT_ADMIN)
       Change Password (SELF, ROLE_TENANT_ADMIN)
       Search (ROLE_USER for sharing)
     UserGroup:
       Create (ROLE_ORG_ADMIN)
         invalid: dup name
       Read   (ROLE_USER & ENTITLED)
       Search (ROLE_USER & ENTITLED)
       Update (OWNER)
         invalid: dup name
       Delete (OWNER)
     Encrypt  (ROLE_USER)
     Profile  (OWNER)
     Profile (Deprecated)
     Orgs (Deprecated)
  - CloudProvider
      Endpoint (20+ types)
        Create (ROLE_CLOUD_*, ROLE_USER)
          invalid: dup name
        Read   (OWNER, ENTITLED)
        Search (OWNER)
        ENTITLE
        Update (OWNER)
        Delete (OWNER)
      CloudProvider
        FindAll: Region, Images, InstanceTypes (ENTITLED)
  - Policies:
      Quota
        Create
          invalid: dup name
        Read
        Search
        Update
        Delete
      Cost
        Create
          invalid: dup name
        Read
        Search
        Update
        Delete
      Approval
        Create
        Read
        Search
        Update
        Delete
  - Apps
     Search (OWNER & ENTITLED)
     Provision
       invalid: empty containers, empty cluster, customizations, owner, lease
       deploy 1 tier app and check
       deploy 3 teir app and check
       deploy app with data binding
       deploy app with plugins
       deploy app with storage
       deploy app with lifecycle
       deploy app with adv plugins
       deploy app with adv placement
       deploy app with adv mem
       deploy app with adv dependencies
     Start
       invalid: empty containers
     Stop
       invalid: empty containers
     Edit
     Add Managers
     Lease
       invalid: lessor
     Move
     Add
     Plugin
     PluginProfile
     Redeploy
     RedeployProfile
     Continuous Delivery
     ScaleOut
     ScaleOutProfile
     ScaleIn
     ScaleInProfile
     Destroy
     Delete
  - AppTasks
     ChangePriority
     ChangeStatus
     FindAll
  - Cluster
     Create
       invalid: dup name, autoscale machineId, max, min
     Read
     Search
     Entitle
     Activate
     Update
     Delete
  - Machine
     Create
       invalid:
     deploy
       support 18+ different platforms & OS's
       check plugin run is supported

     Read
     Search
     Entitle
     Update
     Delete
  - Plugin
     Create
       invalid: dup name
     Read
     Search
     Entitle
     Update
     Delete
     History
  - Message
     FindAll
     Archive
     Approve/Reject
     Read
  - CLI
     Run
  - Build
     Create
       invalid: dup name
     Read
     Entitle
     Update
     Delete
     Build
     Tasks
  - Blueprint
     Create
       invalid: dup name
     Read
     Update
     Delete
  - AppConfig (Not important)
  - Reports


  Governance: Owner, Entitled, Role
  Understand Customer/End User

  JUnit vs TestNG
    - @Suite & @Runwith
    - @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    - @Parameterized
    - @Category
    - Timeout @Test(timeout=1000)

    - Rules
    - Exception

http://junit.org/junit4/


  