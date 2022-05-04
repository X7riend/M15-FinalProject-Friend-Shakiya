package com.company.FinalProject;

import com.company.FinalProject.Crypto.CryptoResponse;
import com.company.FinalProject.ISS.SpaceResponse;
import com.company.FinalProject.Weather.WeatherResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.server.DelegatingServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;
import java.util.Scanner;

@SpringBootApplication
public class FinalProjectApplication {

	public static void main(String[] args) {
		System.out.println("Hello, Welcome to Shakiya's Info Seeker ^_^ ");
		System.out.println(" ");
		System.out.println("Below you have 3 topics to find information about, please select one:");
		System.out.println(" ");
		System.out.println("A.)Info on a city's weather");
		System.out.println(" ");
		System.out.println("B.)Info on cryptocurrency prices");
		System.out.println(" ");
		System.out.println("C.)Info on the location of the International Space Station in latitude and longitude");
		System.out.println(" ");
		System.out.println("D.)Weather in the location of the International Space Station ");
		System.out.println(" ");
		System.out.println("E.)Exit App ");


		String cityName;
		Scanner scanner = new Scanner(System.in);
		String selection = scanner.nextLine();
		SpaceResponse spaceResponse = null;
		switch (selection) {
			case "A": {
				System.out.println("Enter the name of the city whose weather you want info on");
				cityName = scanner.nextLine();

				WebClient client = WebClient.create(String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=da07c762800a66cf5e75b67466369d8b", cityName));


				WeatherResponse weatherResponse = null;
				try {
					Mono<WeatherResponse> response;
					response = client
							.get()
							.retrieve()
							.bodyToMono(WeatherResponse.class);

					weatherResponse = response.share().block();
				} catch (WebClientResponseException wcre) {
					int statusCode = wcre.getRawStatusCode();
					if (statusCode >= 400 && statusCode < 500) {
						System.out.println("Client Error");
					} else if (statusCode >= 500 && statusCode < 600) {
						System.out.println("Server Error");
					}
					System.out.println("Message: " + wcre.getMessage());
				} catch (Exception e) {
					System.out.println("An error has occurred: " + e.getMessage());
				}
				System.out.println("You have indicated the location of: " + weatherResponse.name + ", " + weatherResponse.sys.country);
				System.out.println(cityName + " has weather that consist of: " + weatherResponse.weather[0].description);

				break;
			}
			case "B": {
				System.out.println("Enter the Symbol of the cryptocurrency");
				String cryptoSym = scanner.nextLine();
				WebClient client = WebClient.create(String.format("https://rest.coinapi.io/v1/assets/%s?apikey=BADE6E39-3379-431C-9FC2-C42FF94A45FC", cryptoSym));

				CryptoResponse[] cryptoResponse = new CryptoResponse[0];
				try {
					Mono<CryptoResponse[]> response2;
					response2 = client
							.get()
							.retrieve()
							.bodyToMono(CryptoResponse[].class);

					cryptoResponse = response2.share().block();
				} catch (WebClientResponseException we) {
					int statusCode = we.getRawStatusCode();
					if (statusCode >= 400 && statusCode < 500) {
						System.out.println("Client Error");
					} else if (statusCode >= 500 && statusCode < 600) {
						System.out.println("Server Error");
					}
					System.out.println("Message: " + we.getMessage());
				} catch (Exception e) {
					System.out.println("An error has occurred: " + e.getMessage());
				}

				System.out.println("The name of the currency is " + cryptoResponse[0].name);
				System.out.println("The currency's symbol is " + cryptoSym);
				float form = cryptoResponse[0].price_usd;
				Locale USD = new Locale("en", "US");
				Currency dollars = Currency.getInstance(USD);
				NumberFormat fmt = NumberFormat.getCurrencyInstance(USD);
				System.out.println("The price of the currency is " + fmt.format(cryptoResponse[0].price_usd));
				break;
			}
			case "C": {
				WebClient client = WebClient.create("http://api.open-notify.org/iss-now.json");

				Mono<SpaceResponse> response3;
				response3 = client
						.get()
						.retrieve()
						.bodyToMono(SpaceResponse.class);

				spaceResponse = response3.share().block();


				System.out.println("The latitude of the ISS is: " + spaceResponse.iss_position.latitude);
				System.out.println("The longitude of the ISS is: " + spaceResponse.iss_position.longitude);
				if (spaceResponse.iss_position.latitude == null || spaceResponse.iss_position.longitude == null) {
					System.out.println("The ISS is not in the country");
				}

				break;
			}
			case "D": {
				WebClient clientSF = WebClient.create("http://api.open-notify.org/iss-now.json");

				try {
					Mono<SpaceResponse> responseZ;
					responseZ = clientSF
							.get()
							.retrieve()
							.bodyToMono(SpaceResponse.class);

					SpaceResponse spaceResponse2 = responseZ.share().block();

					String lat = spaceResponse2.iss_position.latitude;
					String lon = spaceResponse2.iss_position.longitude;


					WebClient clientSP = WebClient.create(String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=da07c762800a66cf5e75b67466369d8b", lat, lon));

					WeatherResponse weatherResponse2 = null;
					Mono<WeatherResponse> responseSP;
					responseSP = clientSP
							.get()
							.retrieve()
							.bodyToMono(WeatherResponse.class);

					weatherResponse2 = responseSP.share().block();
					System.out.println("The latitude of the ISS is: " + spaceResponse2.iss_position.latitude);
					System.out.println("The longitude of the ISS is: " + spaceResponse2.iss_position.longitude);
					System.out.println("The coordinates point to " + weatherResponse2.name + ", " + weatherResponse2.sys.country);
					System.out.println("The weather in the location consist of: " + weatherResponse2.weather[0].description);
				} catch (WebClientResponseException wcre) {
					int statusCode = wcre.getRawStatusCode();
					if (statusCode >= 400 && statusCode < 500) {
						System.out.println("Client Error");
					} else if (statusCode >= 500 && statusCode < 600) {
						System.out.println("Server Error");
					}
					System.out.println("Message: " + wcre.getMessage());
				} catch (Exception e) {
					System.out.println("An error has occurred: " + e.getMessage());

				}
				break;
			}
			case "E": {
				System.out.println("You are exiting the application. Goodbye!");
			break;
			}
			default:
				System.out.println("You have not entered a valid selection.");
		}
	}
}



