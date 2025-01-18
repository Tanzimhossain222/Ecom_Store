### Steps to Deploy a Spring Boot Application on AWS

1. **Create AWS Account & Login**
   - Sign up for an AWS account at [aws.amazon.com](https://aws.amazon.com/).
   - Log in to the AWS Management Console.

2. **Create User & Role**
   - Navigate to the IAM (Identity and Access Management) service.
   - Create a new user with programmatic access.
   - Attach the necessary policies to the user.
   - Create a role for EC2 with the following policies:
      - `ElasticBeanstalkWebTier`
      - `ElasticBeanstalkWorkerTier`
      - `AWSElasticBeanstalkMulticontainerDocker`

3. **Set Access Key & Secret Key**
   - Generate an access key and secret key for the created user.
   - Configure these keys in your local environment or CI/CD pipeline.

4. **Create S3 Bucket and Configure**
   - Navigate to the S3 service.
   - Create a new S3 bucket.
   - Configure the bucket with the necessary permissions and policies.
   - Create 3 buckets:
      - for profile
      - Category
      - Products

5. **Create Database in RDS**
   - Navigate to the RDS (Relational Database Service).
   - Create a new database instance.
   - Configure the database with the necessary settings (e.g., instance type, storage, security groups).

6. **Set Database Details in Spring Boot Application**
   - Update the `application.properties` or `application.yml` file in your Spring Boot application with the RDS database details.

7. **Create a JAR File**
   - Build your Spring Boot application using Maven or Gradle to generate a JAR file.

8. **Create Environment & Configure in Elastic Beanstalk**
   - Navigate to the Elastic Beanstalk service.
   - Create a new environment for your application.
   - Configure the environment with the necessary settings (e.g., instance type, security groups).

9. **Upload JAR File in Elastic Beanstalk**
   - Upload the generated JAR file to the Elastic Beanstalk environment.
   - Deploy the application.

### Create Role for EC2
- Select the following policies for the role:
   - `ElasticBeanstalkWebTier`
   - `ElasticBeanstalkWorkerTier`
   - `AWSElasticBeanstalkMulticontainerDocker`

### Elastic Beanstalk
- Use Elastic Beanstalk to manage the deployment and scaling of your application.

### Database RDS
- Use RDS to manage your relational database in the cloud.