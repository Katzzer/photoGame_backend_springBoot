# Photo Game - Backend Application written in Java with Spring Boot

## TODO
- Add tests to JWT
- Repair plugin for Docker in Maven

## Connect on Linux to remote directory
- [Manual](https://www.linode.com/docs/guides/linux-mount-smb-share/)
- `sudo apt install cifs-utils psmisc`
- `sudo mkdir /mnt/photoGamePhotos`
- On Raspberry Pi default shared folder is `sambashare ` login is `guest` and password is `guest`
- Mount directory `sudo mount -t cifs //192.168.1.20/sambashare /mnt/photoGamePhotos -o username=guest,password=guest,uid=1000,gid=1000,file_mode=0775,dir_mode=0775`
- Use `dev` profile on linux

## Module description
- api - used for api communication
- if used profile `h2` (a h2 database) than couples of photos are saved to database and images are saved to `temp` directory when app shutdown all images in `temp` directories are deleted

## How to run this application
- Run module `api`, it runs on port 8080

## More info
- H2 database runs on `http://localhost:8080/h2-console`
- Basic api url is `http://localhost:8080/api/v1/data`

## Test review
- To review test coverage run `mvn test` (use command line or Maven action in IntelliJ Idea)
- Go to `api/target/site/jacoco/index.html` to see code coverage

## Docker

## Create Docker image
- To push image to Dockerhub you have to be logged with Dockerhub account (we won't use this way in future)
- To create docker image go to the directory where is selected microservice and type:
`mvn clean package -P build-docker-image`
  - In IntelliJ Idea go to Maven - select profile `build-docker-image`. In lifecycle click on `package`.
- This will create and push image to Dockerhub

### Run all application
- for that is needed only `docker-compose.yml` file
- In root directory of this project in command line type `docker-compose -f docker-compose.yml up -d`
- To stop app type `docker-compose down`

## Swagger Documentation
- Go to page [localhost:8080/swagger-ui/](http://localhost:8080/swagger-ui/)

## External GPS photo check:
- Go to page [Position stack](https://positionstack.com/)
- Api Access key is `9a78eefebbaee92c86702374251227ac`
- Example request: `http://api.positionstack.com/v1/forward?access_key=9a78eefebbaee92c86702374251227ac&query=Hradec Kralove`

