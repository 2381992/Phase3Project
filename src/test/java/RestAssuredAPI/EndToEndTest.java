package RestAssuredAPI;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EndToEndTest {
	RequestSpecification request;
	Response response;
	int responseCode, responseCode1;
	Map<String, Object> MapObj;

	@Test
	public void Test() {
		RestAssured.baseURI = "http://localhost:3000";
		request = RestAssured.given();

		// Get:To List all Employees Details
		response = GetAllEmployees();
		Assert.assertEquals(200, responseCode);

		// Post:To Create New Employee Details
		response = CreateTheEmployee("John", "8000");
		JsonPath jpath = response.jsonPath();
		int ID = jpath.get("id");
		System.out.println("The New ID Created is:" + ID);
		Assert.assertEquals(201, responseCode1);

		// Get:To Get Single Employee Detail
		response = GetSingleEmployees(ID);
		String Name = jpath.get("name");
		System.out.println("The Name is :" + Name);
		Assert.assertEquals("John", Name);
		Assert.assertEquals(200, responseCode);

		// Patch:To Update Details of any Employee
		response = UpdateTheEmployees("Smith", ID);
		Assert.assertEquals(200, responseCode);

		// Get:To Get Single Employee Detail After Updating
		response = GetSingleEmployees(ID);
		JsonPath jpath1 = response.jsonPath();
		String CName = jpath1.get("name");
		Assert.assertEquals(CName, "Smith");
		System.out.println("The Name is :" + CName);
		Assert.assertEquals(200, responseCode);

		// Delete:To Delete Details of an Employee
		response = DeleteTheEmployee(ID);
		Assert.assertEquals(200, responseCode);

		// Get:To Get Single Employee Detail After Deleting
		response = GetSingleEmployees(ID);
		Assert.assertEquals(404, responseCode);

		// Get:To CrossCheck the Deleted Detail
		response = GetAllEmployees();
		JsonPath jpath2 = response.jsonPath();
		List<String> Names = jpath2.get("name");
		Assert.assertNotEquals(Names.get(0), "Smith");
		Assert.assertEquals(200, responseCode);
	}

	public Response GetAllEmployees() {

		response = request.get("employees");
		System.out.println(response.getBody().asString());
		responseCode = response.getStatusCode();
		return response;

	}

	public Response CreateTheEmployee(String Name, String Salary) {

		MapObj = new HashMap<String, Object>();
		MapObj.put("name", Name);
		MapObj.put("salary", Salary);

		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(MapObj).post("employees/create");
		System.out.println(response.body().asString());
		responseCode1 = response.getStatusCode();

		return response;

	}

	public Response GetSingleEmployees(int EID) {

		response = request.get("employees/" + EID);
		System.out.println(response.getBody().asString());
		responseCode = response.getStatusCode();
		return response;

	}

	public Response UpdateTheEmployees(String Name, int EID) {

		MapObj = new HashMap<String, Object>();
		MapObj.put("name", Name);
		MapObj.put("id", EID);

		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).body(MapObj)
				.patch("employees/" + EID);
		System.out.println(response.getBody().asString());
		responseCode = response.getStatusCode();
		return response;

	}

	public Response DeleteTheEmployee(int EID) {

		response = request.contentType(ContentType.JSON).accept(ContentType.JSON).delete("employees/" + EID);
		System.out.println(response.getBody().asString());
		responseCode = response.getStatusCode();
		return response;

	}

}
