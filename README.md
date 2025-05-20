<a name="readme-top"></a>
<div align="center">
  <img src="fourth.jpg" alt="Description of the image">
    <br>
  <h1>HopeConnect -🇵🇸</h1>
  <strong>Welcome to HopeConnect – Empowering Lives, One Child at a Time 💜</strong> &nbsp;<br>
  <br>
  <img src="third.jpg" alt="Description of the image">

</div>
<br>
<div align="center">

<p>Connecting donors, sponsors, and volunteers with orphanages in Gaza to build a future of hope, care, and opportunity. 💜🌍🤝</p>
<br>

  <a href="https://github.com/FatinaAlTaherr/HopeConnect.git"><strong>Check out the documents »</strong></a>
</div>

<br>



<details>
  <summary><h2>💜 Table of Contents<h2\></summary>
  <ol>
    <li><a href="#intro">Introduction (What's HopeConnect?)</a></li>
    <li><a href="#coref">Core Features</a></li>
    <li><a href="#addf">Additional Features</a></li>
    <li><a href="#roles">Roles</a></li>
    <li><a href="#bw">Built With</a></li>
    <li><a href="#gs">Getting Started</a></li>
    <li><a href="#API">API Documentation</a></li>
    <li><a href="#demo">Demo</a></li>
    <li><a href="#contribution">Contribution</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>
 <br>



<a name="intro"></a>
## 💜 What is HopeConnect?
<strong>HopeConnect</strong> is a platform dedicated to supporting orphaned children in Gaza by connecting donors, sponsors, and volunteers with verified orphanages and NGOs. It serves as a transparent and secure hub for facilitating sponsorships, donations, and essential services—ensuring that aid reaches the children who need it most. HopeConnect empowers individuals and organizations to make a tangible impact through compassionate giving, service, and community-driven support.
<br>
<br>
<br>



<a name="coref"></a>
## 💜 Core Features
<strong>1. Orphan Profiles & Sponsorships:</strong> Detailed profiles for each child including age, education, and health. Donors can sponsor children and receive updates on their progress, photos, and wellbeing. <br>
<strong>2. Donation Management System:</strong> Supports various donation types—money, clothes, food, education materials—with dedicated categories like General Fund, Education Support, and Medical Aid. Includes integrated payment systems and donation tracking. <br>
<strong>3. Volunteer & Service Matching:</strong> Enables volunteers to register and match with orphanages requesting services such as healthcare, mentoring, or teaching. <br>
<strong>4. Transparency & Trust:</strong> Donor dashboards with spending reports, verified NGOs and orphanages, and a review system for accountability. <br>
<strong>5. Emergency Relief Support:</strong> Dedicated system for responding to urgent crises with campaign updates, donation options, and email alerts. <br>
<strong>6. Logistics & Distribution Tracking:</strong> Real-time mapping and coordination of donation deliveries and physical resources. <br>
▶️ For more details on <strong>Core Features</strong> visit the <a href="https://github.com/FatinaAlTaherr/HopeConnect/wiki">Wiki</a> section.
 <br>
 <br>
 <br>
 <br>

 
<a name="addf"></a>
## 💜 Additional Features
<strong>1. Chat System: </strong>Events group chats enable volunteers and representatives to communicate within event-specific group chats in real-time , enhancing coordination and collaboration within the event community.<br>
<strong>2. External API Integration:</strong> Leverages external APIs for data enrichment, location mapping, or medical resource validation to enhance service accuracy.<br>
<strong>3. Security: </strong>Privacy, security and encryption are provided as all passwords are encrypted. For more details visit our <a href="https://github.com/FatinaAlTaherr/HopeConnect/wiki">Wiki.</a> <br>
<strong>4. Testing: </strong> Guaranteeing a robust and stable platform for crafting project management.<br>
<strong>5. Docker: </strong> Simplifies software deployment by packaging applications into portable containers.<br>

 <br>
 <p align="right">(<a href="#readme-top">⬆️ Back to top</a>)</p>
 <br>
 <br>


<a name="roles"></a>
## 💜 Roles
Roles are actually like digital security guards. They check user IDs and say "yes" or "no" to some actions, like deleting gardens or viewing other people's stuff. This keeps everyone safe and in their own areas of the app. In this project, we implemented the following roles:
* <strong>Admin:</strong> Full access to the system. Admins can manage users, orphanages, campaigns, donations, and approve or reject organization registrations. They oversee the platform’s transparency, security, and compliance.<br>
* <strong>Donor:</strong> Can browse orphan profiles, sponsor children, donate to specific causes (education, medical, emergency), and view the impact of their contributions through reports and dashboards.<br>
* <strong>Sponsor:</strong> A specialized donor role focused on long-term sponsorship of individual children. Can receive regular updates about the child’s progress and well-being.<br>

* <strong>Volunteer:</strong> Can register and manage their volunteering profile, browse available service opportunities, and apply to assist orphanages based on their skills (e.g., teaching, healthcare, mentoring).<br>
* <strong>Orphanage Owner:</strong> Can manage orphan profiles, post service requests, update donation needs, and communicate with donors and volunteers. They also have access to analytics on resources and logistics.<br>
 <p align="right">(<a href="#readme-top">⬆️ Back to top</a>)</p>
 <br>
 <br>

 
<a name="bw"></a>
## 💜 Built With
* [![SpringBoot][Spring-boot]][SpringURL] <br>An open-source Java framework for creating stand-alone, production-grade applications.
* [![MySQL][MySQL]][MySQLURL] <br>A reliable, open-source relational database management system commonly used for storing and managing data in web applications.
* [![Rabbit][Rabbit]][MySQLURL] <br>A message broker that enables asynchronous communication between services through messaging queues.
* [![Docker][Docker]][DockerURL] <br>A platform for building, sharing, and running applications in containers.
* [![Postman][Postman]][PostmanURL] <br>A collaboration platform for designing, testing, and documenting APIs.
* [![Github][Github]][GithubURL] <br>A web-based platform for version control and collaboration using Git.
<br>
<p align="right">(<a href="#readme-top">⬆️ Back to top</a>)</p>
<br>




<a name="gs"></a>
## 💜 Getting Started
### ⚙️ Running the project
#### To get started with the project:
##### 1. Clone the repository:
> [![Github][Github]][wewe]
>
> ```sh
> git clone https://github.com/FatinaAlTaherr/HopeConnect.git
> ```
##### 2. Install Dependencies
Make sure you have Maven installed. Run the following command to install the necessary dependencies:
>
> ```sh
> mvn clean install
> ```
##### 3. Create The Database:
* Make sure MySQL is installed and running on your local machine.
* Create a new database for the project:
>
> ```sh
> CREATE DATABASE hopeconnect;
> ```
* Update the application.properties or application.yml file in the src/main/resources directory with your MySQL database credentials (username and password).
>
> ```sh
> spring.datasource.url=jdbc:mysql://localhost:3306/hopeconnect
> spring.datasource.username=your_mysql_username
> spring.datasource.password=your_mysql_password
> ```
##### 4. Run The Application:
>
> ```sh
> mvn spring-boot:run
> ```
<br>
<br>
<br>



 <a name="coref"></a>
<br>
<p align="right">(<a href="#readme-top">⬆️ Back to top</a>)</p>
<br>


<a name="contact"></a>
## 💜 Contact

**Feel free to reach out!**  
🌟 I'm always happy to connect, collaborate, or answer any questions.

**Email:** [Fatina@gmail.com](mailto:Fatina@gmail.com)

**Follow me:**  
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)](https://github.com/FatinaAlTaherr?tab=repositories)  





<!-- MARKDOWN LINKS & IMAGES -->
[Spring-boot]: https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white
[SpringURL]: https://spring.io/projects/spring-boot
[Docker]: https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white
[DockerURL]: https://www.docker.com/
[MySQL]: https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white
[MySQLURL]: https://www.mysql.com/
[Rabbit]: https://img.shields.io/badge/RabbitMQ-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white
[RabbitURL]: https://www.rabbitmq.com/
[Github]: https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white
[GithubURL]: https://github.com/
[Postman]: https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white
[PostmanURL]: https://www.postman.com/
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[wewe]: https://github.com/Mohammad-Aker/GreenThumb
[JQuery-url]: https://jquery.com 

