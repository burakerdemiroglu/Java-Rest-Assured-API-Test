import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

public class Github {
    String baseUrl = "https://api.github.com";
    String username = "burakerdemiroglu";
    String repoName = "Java101";
    String token = "ornek-Token";

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
                        statusCode(200). // status kodu 200 geldiğini doğrular
                        time(lessThan(3000L)). // istek süreninin maksimum 3 saniye olmasını söyler
                        extract().response();

//        System.out.println(response.path("").toString());

        String name = response.path("name").toString();
        String blog = response.path("blog").toString();
        String id = response.path("id").toString();

        Assert.assertEquals(name, "Burak ERDEMİROĞLU");
        Assert.assertEquals(blog, "https://www.linkedin.com/in/burakerdemiroglu/");
        Assert.assertEquals(id, "35409987");
    }

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
}