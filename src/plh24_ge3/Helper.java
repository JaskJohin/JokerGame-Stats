package plh24_ge3;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Contains static methods that are used by the other classes.
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class Helper
{
	// Methods
	/**
	 * Connects to the API url and gets the json string that the API returns.
	 * @param urlStr   The API url.
	 * @return         The json string that the API returns.
	 */
	static String getJsonStrFromApiURL(String urlStr) throws Exception
	{
		String jsonStr;

		// URL
		URL website = new URL(urlStr);

		// Start connection and set timeout to 2 seconds
		URLConnection connection = website.openConnection();
		connection.setConnectTimeout(2*1000);

		// Open BufferedReader
		InputStreamReader isr = new InputStreamReader(connection.getInputStream());
		BufferedReader in = new BufferedReader(isr);

		// Get json string
		jsonStr = in.readLine();

		// Close BufferedReader
		in.close();

		return jsonStr;
	}


	/** 
	 * Accepts as parameter a JsonObject with data of a single draw of the game Joker
	 * and returns a JokerDrawData object with all the extracted data.
	 * @param jObject   A JsonObject with data of a single draw of the game Joker.
	 * @return          A JokerDrawData object with all the extracted data.
	 */
	static JokerDrawData jokerJsonSingleDrawToObject(JsonObject jObject)
	{
		// BigDecimal - used for rounding
		BigDecimal bd;

		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Variables to gather data
		int drawId;
		String drawDate;
		int columns;
		int winningNum1;
		int winningNum2;
		int winningNum3;
		int winningNum4;
		int winningNum5;
		int bonusNum;
		int prizeTier5_1winners;
		double prizeTier5_1dividend;
		int prizeTier5winners;
		double prizeTier5dividend;
		int prizeTier4_1winners;
		double prizeTier4_1dividend;
		int prizeTier4winners;
		double prizeTier4dividend;
		int prizeTier3_1winners;
		double prizeTier3_1dividend;
		int prizeTier3winners;
		double prizeTier3dividend;
		int prizeTier2_1winners;
		double prizeTier2_1dividend;
		int prizeTier1_1winners;
		double prizeTier1_1dividend;


		// Get the drawId
		drawId = jObject.get("drawId").getAsInt();

		// Get the drawTime
		Long drawTime = jObject.get("drawTime").getAsLong();
		LocalDateTime ldt = Instant.ofEpochMilli(drawTime).atZone(ZoneId.systemDefault()).toLocalDateTime();
		drawDate = formatter.format(ldt);

		// Get the number of columns
		JsonObject wagerStatistics = jObject.getAsJsonObject("wagerStatistics");
		columns = wagerStatistics.get("columns").getAsInt();

		// Get the winning numbers
		JsonObject winningNumbers = jObject.getAsJsonObject("winningNumbers");
		JsonArray winningNumList = winningNumbers.getAsJsonArray("list");
		winningNum1 = winningNumList.get(0).getAsInt();
		winningNum2 = winningNumList.get(1).getAsInt();
		winningNum3 = winningNumList.get(2).getAsInt();
		winningNum4 = winningNumList.get(3).getAsInt();
		winningNum5 = winningNumList.get(4).getAsInt();
		JsonArray winningNumBonus = winningNumbers.getAsJsonArray("bonus");
		bonusNum = winningNumBonus.get(0).getAsInt();

		// Prize categories
		JsonArray prizeCategories = jObject.getAsJsonArray("prizeCategories");

		// Get the prize tier "5+1" winners & dividend
		JsonObject category0 = prizeCategories.get(0).getAsJsonObject();
		prizeTier5_1winners = category0.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category0.get("divident").getAsDouble());
		prizeTier5_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "5" winners & dividend
		JsonObject category1 = prizeCategories.get(1).getAsJsonObject();
		prizeTier5winners = category1.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category1.get("divident").getAsDouble());
		prizeTier5dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "4+1" winners & dividend
		JsonObject category2 = prizeCategories.get(2).getAsJsonObject();
		prizeTier4_1winners = category2.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category2.get("divident").getAsDouble());
		prizeTier4_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "4" winners & dividend
		JsonObject category3 = prizeCategories.get(3).getAsJsonObject();
		prizeTier4winners = category3.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category3.get("divident").getAsDouble());
		prizeTier4dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "3+1" winners & dividend
		JsonObject category4 = prizeCategories.get(4).getAsJsonObject();
		prizeTier3_1winners = category4.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category4.get("divident").getAsDouble());
		prizeTier3_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "3" winners & dividend
		JsonObject category5 = prizeCategories.get(5).getAsJsonObject();
		prizeTier3winners = category5.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category5.get("divident").getAsDouble());
		prizeTier3dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "2+1" winners & dividend
		JsonObject category6 = prizeCategories.get(6).getAsJsonObject();
		prizeTier2_1winners = category6.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category6.get("divident").getAsDouble());
		prizeTier2_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

		// Get the prize tier "2+1" winners & dividend
		JsonObject category7 = prizeCategories.get(7).getAsJsonObject();
		prizeTier1_1winners = category7.get("winners").getAsInt();
		bd = BigDecimal.valueOf(category7.get("divident").getAsDouble());
		prizeTier1_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();


		// Convert the first 209 divident from drachma to euro
		if (drawId <= 209 && prizeTier1_1dividend > 100)
		{
			bd = BigDecimal.valueOf(prizeTier5_1dividend/340.75);
			prizeTier5_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier5dividend/340.75);
			prizeTier5dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier4_1dividend/340.75);
			prizeTier4_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier4dividend/340.75);
			prizeTier4dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier3_1dividend/340.75);
			prizeTier3_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier3dividend/340.75);
			prizeTier3dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier2_1dividend/340.75);
			prizeTier2_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			bd = BigDecimal.valueOf(prizeTier1_1dividend/340.75);
			prizeTier1_1dividend = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
		}

		// Create JokerDrawData object
		JokerDrawData jokerDraw = new JokerDrawData(drawId, drawDate, columns,
			winningNum1, winningNum2, winningNum3, winningNum4, winningNum5, bonusNum,
			prizeTier5_1winners, prizeTier5_1dividend,
			prizeTier5winners, prizeTier5dividend,
			prizeTier4_1winners, prizeTier4_1dividend,
			prizeTier4winners, prizeTier4dividend,
			prizeTier3_1winners, prizeTier3_1dividend,
			prizeTier3winners, prizeTier3dividend,
			prizeTier2_1winners, prizeTier2_1dividend,
			prizeTier1_1winners, prizeTier1_1dividend);

		return jokerDraw;
	}


	/**
	 * Accepts the gameId and a date range as parameters, breaks the date range to periods
	 * of up to 92 days and creates a list with the appropriate URL strings of the api
	 * "https://api.opap.gr/draws/v3.0/{gameId}/draw-date/{fromDate}/{toDate}".
	 * @param gameId    The game id.
	 * @param fromDate  The start date.
	 * @param toDate    The end date.
	 * @return  A list with the URL strings.
	 */
	static List<String> getUrlStrForDateRange(int gameId, String fromDate, String toDate)
	{
		// Variables
		String urlStr;
		List<String> urlStrList = new ArrayList<>();  // List with all the url we'll call

		// Date format
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		// Convert the two selected "yyyy-MM-dd" dates to LocalDate
		LocalDate ld1 = LocalDate.parse(fromDate, formatter);
		LocalDate ld2 = LocalDate.parse(toDate, formatter);

		// Max date range to call the API
		int max = 92;


		// Create the list with all the API urls we'll need to call
		LocalDate start = ld1;
		boolean finished = false;
		while (finished == false)
		{
			// Start date
			String date1 = formatter.format(start);

			// End date
			LocalDate end = start.plusDays(max);
			long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(end, ld2);

			// Fix end date if it overshoots the selected date range
			if (daysBetween <= 0)
			{
				end = ld2;
				finished = true;  // Exit the loop after this
			}
			String date2 = formatter.format(end);

			// Create the url string
			urlStr = "https://api.opap.gr/draws/v3.0/" + gameId + "/draw-date/" + date1 + "/" + date2 + "/?limit=180";

			// Add url to urlStrList
			urlStrList.add(urlStr);

			// New start date
			start = end.plusDays(1);
		}

		// Return the list with the url strings
		return urlStrList;
	}
}
