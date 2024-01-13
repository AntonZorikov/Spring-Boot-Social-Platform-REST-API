# Spring-Boot-Social-Platform-REST-API

## Description
This project is a RESTful API developed using the Spring Boot framework. The API provides a series of endpoints for performing specific operations and interacting with data.

## Technologies
- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Hibernate
- Maven

## Install
1. `git clone https://github.com/AntonZorikov/Spring-Boot-Social-Platform-REST-API`
2. `cd Spring-Boot-Social-Platform-REST-API/jeddit`
3. `mvn clean install`
4. `java -jar target/***.jar`

## Api
### Auth
| Method | Url | Description | Request Body |
| ------ | --- | ----------- | --------------------------- | 
| POST   | /api/auth/signin | Sign in to the application | [JSON](#signin) | 
| POST   | /api/auth/register | Create a new account | [JSON](#register) | 

### Users
| Method | Url | Description | Request Body |
| ------ | --- | ----------- | --------------------------- | 
| PUT   | api/users/{id}/change_password | Change user password | [JSON](#change_password) | 
| GET   | /api/users/{id}/base_info | Get basic information about the user |  | 
| GET   | /api/users/{id}/all_info | Get complete information about the user | [JWT JSON](#jwt) | 
| DELETE   | /api/users/{id} | Delete the account | [JWT JSON](#jwt) | 
| GET   | api/users/{id}/posts | Get information about user posts |  | 
| GET   | /api/users/newsline | Get the user's news feed | [JWT JSON](#jwt) | 

### Posts
| Method | Url | Description | Request Body |
| ------ | --- | ----------- | --------------------------- | 
| POST   | /api/posts/ | Create a post | [JSON](#create_post) | 
| GET   | /api/posts/{id} | Get information about the post |  | 
| PUT   | api/posts/{id} | Edit the post | [JSON](#put_post) | 
| DELETE   | /api/posts/{id} | Delete the post| [JWT JSON](#jwt) | 
| POST   | /api/posts/{id}/upvote | Upvote the post | [JWT JSON](#jwt) | 
| POST   | /api/posts/{id}/downvote | Downvote the post | [JWT JSON](#jwt) | 
| GET   | /api/posts/{id}/vote | Get the vote status for the post for the user | [JWT JSON](#jwt) | 
| DELETE   | /api/posts/{id}/vote | Delete the post | [JWT JSON](#jwt) | 

### Communities
| Method | Url | Description | Request Body |
| ------ | --- | ----------- | --------------------------- | 
| POST   | /api/communities/ | Create a community | [JSON](#create_community) | 
| GET   | /api/communities/{title} | Get information about the community |  | 
| PUT   | /api/communities/{title}/change_description | Change the description of the community | [JSON](#change_description) | 
| POST   | /api/communities/{title}/follow | Subscribe to the community | [JWT JSON](#jwt) | 
| POST   | /api/communities/{title}/unfollow | Unsubscribe from the community | [JWT JSON](#jwt) | 
| GET   | /api/communities/{title}/followers?page={num1}&size={num2} | Get community followers |  | 
| GET   | /api/communities/{title}/posts?page={num1}&size={num2} | Get community posts |  | 

### Commentaries
| Method | Url | Description | Request Body |
| ------ | --- | ----------- | --------------------------- | 
| POST   | /api/commentaries/{community_id}/ | Create a commentary | [JSON](#create_commentary) | 
| GET   | /api/commentaries/{id} | Get information about the commentary |  |
| DELETE   | /api/commentaries/{id} | Delete the commentary | [JWT JSON](#jwt) |

### Search
| Method | Url | Description | Request Body |
| ------ | --- | ----------- | --------------------------- | 
| GET   | /api/search/users?page={num1}&size={num2} | Find a user by name | [SEARCH JSON](#search) | 
| GET   | /api/search/communities?page={num1}&size={num2} | Find a community by name | [SEARCH JSON](#search) | 
| GET   | /api/search/communitiesByDescription?page={num1}&size={num2} | Find communities by description | [SEARCH JSON](#search) | 
| GET   | /api/search/posts?page={num1}&size={num2} | Find a post by title | [SEARCH JSON](#search) | 
| GET   | /api/search/postsByText?page={num1}&size={num2} | Find a post by description | [SEARCH JSON](#search) |

## JSON Request 
##### <a id="jwt">JWT Request</a>
```json
{
    "jwttoken" : "eyJhbGciOiJImlhdCI6MTcwNDM4NTU5NiwiZXhwIjoxNzA0Mzg5MTk2fQgXp04I2bFUKzpvOGfAbbq_0wgqJDRfo0QGDC6CRWTfwhXAeY3n7eK2GEXERaw3dAwKvQxCiW5WoBZ0FiIHB2Xw"
}
```

##### <a id="search">Search Request</a>
```json
{
    "text" : "Some text"
}
```

##### <a id="signin">/api/auth/signup</a>
```json
{
    "login" : "loginTest",
    "password" : "Abcd123"
}
```

##### <a id="register">/api/auth/register</a>
```json
{
    "login" : "loginTest",
    "password" : "Abcd123",
    "email" : "testemail@mail.ru"
}
```

##### <a id="change_password">api/users/{id}/change_password</a>
```json
{
    "jwttoken" : "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJpZCI6MSwiaWF0IjoxNzAzOTQ3NzMzLCJleHAiOjE3MDM5NTEzMzN9.tQi8S3dW8CxSyHnT7TVYdoKZugaFM1lShv4IlgxGATY56j27b5-RWqbKH2LLFhwkZvD5y3nuFc1OTVawhp_lYA",
    "oldPassword" : "passOld",
    "newPassword" : "passNew"
}
```

##### <a id="create_post">/api/posts/</a>
```json
{
    "jwttoken" : "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJpZCI6MTAsImlhdCI6MTcwNDcyODU2OCwiZXhwIjoxNzA0NzMyMTY4fQ.m3Cny__KHZyRwpRZFo9B7ztSWz8VsEzJ84mv3VvhxbPYIayHvEQemj28ye51bdw_eT0vTF9WjnDwTW4Pbl2l9g",
    "title" : "Post about Java",
    "text" : "Post text",
    "communityTitle" : "Java_Community"
}
```

##### <a id="put_post">api/posts/{id}</a>
```json
{
    "jwttoken" : "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJpZCI6OCwiaWF0IjoxNzA0Mjk1NDAzLCJleHAiOjE3MDQyOTkwMDN9.luQxjiYxxl9Wt3Q4KBsQTZ_VyxsS9_DfxPuBq_kSoPpHc_vquh8pZAz1-br3uWYk7RHvl9Vx-AI3aF5micBExQ",
    "text" : "Append text"
}
```

##### <a id="create_community">/api/communities/</a>
```json
{
    "jwttoken" : "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJpZCI6MTEsImlhdCI6MTcwNDcxOTA3NiwiZXhwIjoxNzA0NzIyNjc2fQ.F_-xUhrX7udPd8InePHq3qaO5FX3HvD0MNKQCcAJ8-ERTPRZ35PW0FpBemgSu1zJdRSmp_W7nia2WN5HrqEC2A",
    "title" : "My_community",
    "description" : "some text"
}
```

##### <a id="change_description">/api/communities/{title}/change_description</a>
```json
{
    "jwttoken" : "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJpZCI6MSwiaWF0IjoxNzA0MjA4NTAxLCJleHAiOjE3MDQyMTIxMDF9.OZ33EThhoZXR8ib9W0igz6KGBhj5N1OiX5nGJuRYGNFWeXhtphPPluqyoxqO5R45jDa-ACR9WOHiDGd6FrotWw",
    "newDescription" : "some texеееееееt"
}
```

##### <a id="create_commentary">/api/communities/{title}</a>
```json
{
    "jwttoken" : "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiVVNFUiIsInVzZXJpZCI6MTEsImlhdCI6MTcwNDQ3MDEwMywiZXhwIjoxNzA0NDczNzAzfQ.6quDswxcnMKQVU4FoRkt6eSjgyXy8_DxaYLzq5FRu5DfEhiUgSWqY5NPjSwETTTMnYjvLbSbvfWFUpzxlqHf4g",
    "text" : "Commentary"
}
```

## Models
### User
##### <a id="user_model"></a>
```json
{
    "id": 10,
    "login": "test4",
    "email": "test4@mail.ru",
    "role": "USER",
    "carma": 0
}
```

### Post
##### <a id="post_model"></a>
```json
{
    "id": 8,
    "title": "Post about Java",
    "text": "Some text",
    "date": "2024-01-08T15:47:00.438+00:00",
    "community": {},
    "user": {}
}
```

### Community
##### <a id="community_model"></a>
```json
{
{
    "id": 1,
    "title": "Title",
    "description": "Some text"
}
}
```

### Commentary
##### <a id="commentary_model"></a>
```json
{
{
    "id": 5,
    "text": "Commentary",
    "date": "2024-01-13T16:09:46.322+00:00",
    "owner": {},
    "post": {}
}
}
```