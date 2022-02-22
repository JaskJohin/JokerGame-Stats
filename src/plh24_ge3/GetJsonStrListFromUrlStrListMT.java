package plh24_ge3;

import java.util.ArrayList;
import java.util.List;
import static plh24_ge3.Helper.getJsonStrFromApiURL;


/**
 * Connect to API "https://api.opap.gr/draws/v3.0/{gameId}/draw-date/{fromDate}/{toDate}"
 * and get the json strings that the API returns.
 * Each thread handles a specified range of urlStrList elements.
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
class GetJsonStrListFromUrlStrListMT extends Thread
{
	// Variables declaration
	private int index1 = 0;
	private int index2 = 0;
	private final List<String> urlStrList;
	private final List<String> jsonStrList = new ArrayList<>();


	// Constructor
	/**
	 * Creates a thread that connects to the URL strings from a range of urlStrList
	 * from index1 to index2 - 1, and gets the respective json strings.
	 * @param index1         Start index.
	 * @param index2         End index.
	 * @param urlStrList     Input List that contains the URL strings.
	 */
	public GetJsonStrListFromUrlStrListMT(int index1, int index2, List<String> urlStrList)
	{
		this.index1 = index1;    // Start index. In single thread it would be = 0.
		this.index2 = index2;    // End index. In ST it would be = urlStrList.size().
		this.urlStrList = urlStrList;
	}

	// Methods
	public List<String> getJsonStrList() {return jsonStrList;}

	@Override
	public void run()
	{
		// Call the API urls and get the respective json strings
		for (int i = index1; i < index2; i++)
		{
			// URL string
			String urlStr = urlStrList.get(i);

			try
			{
				// Get json string from the API
				String jsonStr = getJsonStrFromApiURL(urlStr);

				// Add jsonStr to jsonStrList
				jsonStrList.add(jsonStr);
			}
			catch (Exception ex) { /* Silently continue */ }
		}
	}
}
