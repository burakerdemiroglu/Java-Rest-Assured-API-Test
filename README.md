# REST Assured Api Tests
>*libraries*  REST Assured , TestNG  

## Github
**1 - Get User Information**
  
| Request Url  | Request Type |
| ------------- | ------------- |
| https://api.github.com/user  | GET  |

</br>

**Method :**
</br>
```java
   @Test
    void getUser() {
        String requestUrl = baseUrl + "/user";

        Response response =
                given().
                        headers("Authorization", "Bearer " + token).
                        contentType(ContentType.JSON).
                        accept(ContentType.JSON).
                        get(requestUrl).
                        then().
                        statusCode(200).
                        time(lessThan(3000L)). 
                        extract().response();
                        
        String name = response.path("name").toString();
        String blog = response.path("blog").toString();
        String id = response.path("id").toString();

        Assert.assertEquals(name, "Burak ERDEMİROĞLU");
        Assert.assertEquals(blog, "https://www.linkedin.com/in/burakerdemiroglu/");
        Assert.assertEquals(id, "35409987");
    }
    
```

</br>

**2 - Get Repositories**

| Request Url  | Request Type |
| ------------- | ------------- |
| https://api.github.com/users/{{username}}/repos | GET  |

</br>

**Method :**
</br>
```java
     @Test
    void getRepos() {

        String requestUrl = baseUrl + "/users/" + username + "/repos";

        Response response =
                given().
                        headers("Authorization", "Bearer " + token).
                        contentType(ContentType.JSON).
                        accept(ContentType.JSON).
                        get(requestUrl).
                        then().
                        statusCode(200).
                        time(lessThan(3000L)).
                        extract().response();

        int i = 0;
        while (true) {
            String repo = response.path("[" + i + "].name"); //
            String name = response.path("[" + i + "].owner.login");
            i++;

            if (repo == null) { // iteration değeri arttıkça dizi dışına çıkacak ve repo değeri null olacak o sebepten eğer dizi dışına çıkarsa döngüden çıksın
                break;
            } else {
                Assert.assertEquals(name, username);
                System.out.println(i + " - " + repo);
            }
        }
    }
```
</br>

**3 - Get Repository**

| Request Url  | Request Type |
| ------------- | ------------- |
| https://api.github.com/repos/{{username}}/{{repo}} | GET  |

</br>

**Method :**
</br>
```java
     @Test
    void getRepo() {
        String requestUrl = baseUrl + "/repos/" + username + "/" + repoName;

        Response response =
                given().
                        headers("Authorization", "Bearer " + token).
                        contentType(ContentType.JSON).
                        accept(ContentType.JSON).
                        get(requestUrl).
                        then().
                        statusCode(200).
                        time(lessThan(3000L)).
                        extract().response();

        int id = response.path("id");
        String repoName = response.path("name").toString();
        boolean admin = response.path("permissions.admin");

        Assert.assertEquals(id, 545075536);
        Assert.assertEquals(repoName, this.repoName);
        Assert.assertEquals(admin, true);
    }

```
  </br>
  
**4 - Get Readme File**

| Request Url  | Request Type |
| ------------- | ------------- |
| https://api.github.com/repos/{{username}}/{{repo}}/readme | GET  |
</br>

**Method :**</br>
```java
    @Test
    void getReadme() {
        String requestUrl = baseUrl + "/repos/" + username + "/" + this.repoName + "/readme";

        Response response =
                given().
                        headers("Authorization", "Bearer " + token).
                        contentType(ContentType.JSON).
                        accept(ContentType.JSON).
                        get(requestUrl).
                        then().
                        statusCode(200).
                        time(lessThan(3000L)).
                        extract().response();

        String path = response.path("path");
        String type = response.path("type");
        String download_url = response.path("download_url");

        Assert.assertEquals(path, "README.md");
        Assert.assertEquals(type, "file");
        Assert.assertEquals(download_url, "https://raw.githubusercontent.com/burakerdemiroglu/Java101/main/README.md");
    }


```
</br>
    
**5 - Post Repository**

| Request Url  | Request Type |
| ------------- | ------------- |
| https://api.github.com/user/repos | POST  |

</br>

**Method :**</br> 
```java
   @Test
    void postRepo() {

        String requestUrl = baseUrl + "/user/repos";
        String name = RandomStringUtils.randomAlphabetic(5);

        JSONObject request = new JSONObject();
        request.put("name", name);
        request.put("auto_init", "true");
        request.put("private", "true");
        request.put("gitignore_template", "nanoc");

        Response response =
                given().
                        headers("Authorization", "Bearer " + token).
                        contentType(ContentType.JSON).
                        accept(ContentType.JSON).
                        body(request.toJSONString()).
                        when().
                        post(requestUrl).
                        then().
                        statusCode(201).
                        time(lessThan(3000L)).
                        extract().response();

        String repoName = response.path("name").toString();
        String full_name = response.path("full_name").toString();
        String privateField = response.path("private").toString();
       
        Assert.assertEquals(repoName, name);
        Assert.assertEquals(full_name, username + "/" + name);
        Assert.assertEquals(privateField, "true");
    }
    
    
   

```
**6 - Delete Repository**

| Request Url  | Request Type |
| ------------- | ------------- |
| https://api.github.com/repos/{{username}}/{{repo}} | DELETE  |

</br>

**Method :**</br>
```java
    @Test
    void deleteRepo(){
        String repoName = "CRqjT";
        String requestUrl = baseUrl + "/repos/" + username + "/" + repoName;
        given().
                headers("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).
                delete(requestUrl).
                then().
                statusCode(204).
                time(lessThan(3000L));
    }

```
</br>
