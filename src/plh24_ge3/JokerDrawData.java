package plh24_ge3;

/**
 * Data of a single draw of the game Joker.
 * <pre>Constructor:
 * JokerDrawData(int drawId, String drawDate, int columns, int winningNum1,
 *     int winningNum2, int winningNum3, int winningNum4, int winningNum5, int bonusNum,
 *     int prizeTier5_1winners, int prizeTier5_1dividend,
 *     int prizeTier5winners,   int prizeTier5dividend,
 *     int prizeTier4_1winners, int prizeTier4_1dividend,
 *     int prizeTier4winners,   int prizeTier4dividend,
 *     int prizeTier3_1winners, int prizeTier3_1dividend,
 *     int prizeTier3winners,   int prizeTier3dividend,
 *     int prizeTier2_1winners, int prizeTier2_1dividend,
 *     int prizeTier1_1winners, int prizeTier1_1dividend)</pre>
 * <pre>Methods:
 * getDrawId()                  Get the drawId.
 * getDrawDate()                Get the drawDate.
 * getColumns()                 Get the number of columns.
 * getWinningNum1()             Get the 1st winning number.
 * getWinningNum2()             Get the 2nd winning number.
 * getWinningNum3()             Get the 3rd winning number.
 * getWinningNum4()             Get the 4th winning number.
 * getWinningNum5()             Get the 5th winning number.
 * getBonusNum()                Get the bonus winning number.
 * getPrizeTier5_1winners()     Get the number of winners in the "5+1" tier.
 * getPrizeTier5_1dividend()    Get the shares of each winner in the "5+1" tier.
 * getPrizeTier5winners()       Get the number of winners in the "5" tier.
 * getPrizeTier5dividend()      Get the shares of each winner in the "5" tier.
 * getPrizeTier4_1winners()     Get the number of winners in the "4+1" tier.
 * getPrizeTier4_1dividend()    Get the shares of each winner in the "4+1" tier.
 * getPrizeTier4winners()       Get the number of winners in the "4" tier.
 * getPrizeTier4dividend()      Get the shares of each winner in the "4" tier.
 * getPrizeTier3_1winners()     Get the number of winners in the "3+1" tier.
 * getPrizeTier3_1dividend()    Get the shares of each winner in the "3+1" tier.
 * getPrizeTier3winners()       Get the number of winners in the "3" tier.
 * getPrizeTier3dividend()      Get the shares of each winner in the "3" tier.
 * getPrizeTier2_1winners()     Get the number of winners in the "2+1" tier.
 * getPrizeTier2_1dividend()    Get the shares of each winner in the "2+1" tier.
 * getPrizeTier1_1winners()     Get the number of winners in the "1+1" tier.
 * getPrizeTier1_1dividend()    Get the shares of each winner in the "1+1" tier.
 * </pre>
 * 
 * @author Athanasios Theodoropoulos
 * @author Alexandros Dimitrakopoulos
 * @author Odysseas Raftopoulos
 * @author Xristoforos Ampelas
 */
public class JokerDrawData
{
	// Attributes
	private final int drawId;
	private final String drawDate;
	private final int columns;
	private final int winningNum1;
	private final int winningNum2;
	private final int winningNum3;
	private final int winningNum4;
	private final int winningNum5;
	private final int bonusNum;
	private final int prizeTier5_1winners;
	private final double prizeTier5_1dividend;
	private final int prizeTier5winners;
	private final double prizeTier5dividend;
	private final int prizeTier4_1winners;
	private final double prizeTier4_1dividend;
	private final int prizeTier4winners;
	private final double prizeTier4dividend;
	private final int prizeTier3_1winners;
	private final double prizeTier3_1dividend;
	private final int prizeTier3winners;
	private final double prizeTier3dividend;
	private final int prizeTier2_1winners;
	private final double prizeTier2_1dividend;
	private final int prizeTier1_1winners;
	private final double prizeTier1_1dividend;


	// Constructor
	/**
	 * @param drawId                The draw id.
	 * @param drawDate              The draw date.
	 * @param columns               The number of columns.
	 * @param winningNum1           The 1st winning number.
	 * @param winningNum2           The 2nd winning number.
	 * @param winningNum3           The 3rd winning number.
	 * @param winningNum4           The 4th winning number.
	 * @param winningNum5           The 5th winning number.
	 * @param bonusNum              The bonus winning number.
	 * @param prizeTier5_1winners   The number of winners in the "5+1" tier.
	 * @param prizeTier5_1dividend  The shares of each winner in the "5+1" tier.
	 * @param prizeTier5winners     The number of winners in the "5" tier.
	 * @param prizeTier5dividend    The shares of each winner in the "5" tier.
	 * @param prizeTier4_1winners   The number of winners in the "4+1" tier.
	 * @param prizeTier4_1dividend  The shares of each winner in the "4+1" tier.
	 * @param prizeTier4winners     The number of winners in the "4" tier.
	 * @param prizeTier4dividend    The shares of each winner in the "4" tier.
	 * @param prizeTier3_1winners   The number of winners in the "3+1" tier.
	 * @param prizeTier3_1dividend  The shares of each winner in the "3+1" tier.
	 * @param prizeTier3winners     The number of winners in the "3" tier.
	 * @param prizeTier3dividend    The shares of each winner in the "3" tier.
	 * @param prizeTier2_1winners   The number of winners in the "2+1" tier.
	 * @param prizeTier2_1dividend  The shares of each winner in the "2+1" tier.
	 * @param prizeTier1_1winners   The number of winners in the "1+1" tier.
	 * @param prizeTier1_1dividend  The shares of each winner in the "1+1" tier.
	 */
	public JokerDrawData(int drawId, String drawDate, int columns, int winningNum1,
		int winningNum2, int winningNum3, int winningNum4, int winningNum5, int bonusNum,
		int prizeTier5_1winners, double prizeTier5_1dividend,
		int prizeTier5winners,   double prizeTier5dividend,
		int prizeTier4_1winners, double prizeTier4_1dividend,
		int prizeTier4winners,   double prizeTier4dividend,
		int prizeTier3_1winners, double prizeTier3_1dividend,
		int prizeTier3winners,   double prizeTier3dividend,
		int prizeTier2_1winners, double prizeTier2_1dividend,
		int prizeTier1_1winners, double prizeTier1_1dividend)
	{
		this.drawId = drawId;
		this.drawDate = drawDate;
		this.columns = columns;
		this.winningNum1 = winningNum1;
		this.winningNum2 = winningNum2;
		this.winningNum3 = winningNum3;
		this.winningNum4 = winningNum4;
		this.winningNum5 = winningNum5;
		this.bonusNum = bonusNum;
		this.prizeTier5_1winners  = prizeTier5_1winners;
		this.prizeTier5_1dividend = prizeTier5_1dividend;
		this.prizeTier5winners    = prizeTier5winners;
		this.prizeTier5dividend   = prizeTier5dividend;
		this.prizeTier4_1winners  = prizeTier4_1winners;
		this.prizeTier4_1dividend = prizeTier4_1dividend;
		this.prizeTier4winners    = prizeTier4winners;
		this.prizeTier4dividend   = prizeTier4dividend;
		this.prizeTier3_1winners  = prizeTier3_1winners;
		this.prizeTier3_1dividend = prizeTier3_1dividend;
		this.prizeTier3winners    = prizeTier3winners;
		this.prizeTier3dividend   = prizeTier3dividend;
		this.prizeTier2_1winners  = prizeTier2_1winners;
		this.prizeTier2_1dividend = prizeTier2_1dividend;
		this.prizeTier1_1winners  = prizeTier1_1winners;
		this.prizeTier1_1dividend = prizeTier1_1dividend;
	}

	// Methods
	/**
	 * Get the draw id.
	 * @return An int representing the draw id.
	 */
	public int getDrawId() {return this.drawId;}

	/**
	 * Get the draw date.
	 * @return A string representing the draw date.
	 */
	public String getDrawDate() {return this.drawDate;}

	/**
	 * Get the number of columns.
	 * @return An int representing the number of columns.
	 */
	public int getColumns() {return this.columns;}

	/**
	 * Get the 1st winning number.
	 * @return An int representing the 1st winning number.
	 */
	public int getWinningNum1() {return this.winningNum1;}

	/**
	 * Get the 2nd winning number.
	 * @return An int representing the 2nd winning number.
	 */
	public int getWinningNum2() {return this.winningNum2;}

	/**
	 * Get the 3rd winning number.
	 * @return An int representing the 3rd winning number.
	 */
	public int getWinningNum3() {return this.winningNum3;}

	/**
	 * Get the 4th winning number.
	 * @return An int representing the 4th winning number.
	 */
	public int getWinningNum4() {return this.winningNum4;}

	/**
	 * Get the 5th winning number.
	 * @return An int representing the 5th winning number.
	 */
	public int getWinningNum5() {return this.winningNum5;}

	/**
	 * Get the bonus winning number.
	 * @return An int representing the bonus winning number.
	 */
	public int getBonusNum() {return this.bonusNum;}

	/**
	 * Get the number of winners in the "5+1" tier.
	 * @return An int representing the number of winners in the "5+1" tier.
	 */
	public int getPrizeTier5_1winners() {return this.prizeTier5_1winners;}

	/**
	 * Get the shares of each winner in the "5+1" tier.
	 * @return An int representing the shares of each winner in the "5+1" tier.
	 */
	public double getPrizeTier5_1dividend() {return this.prizeTier5_1dividend;}

	/**
	 * Get the number of winners in the "5" tier.
	 * @return An int representing the number of winners in the "5" tier.
	 */
	public int getPrizeTier5winners() {return this.prizeTier5winners;}

	/**
	 * Get the shares of each winner in the "5" tier.
	 * @return An int representing the shares of each winner in the "5" tier.
	 */
	public double getPrizeTier5dividend() {return this.prizeTier5dividend;}

	/**
	 * Get the number of winners in the "4+1" tier.
	 * @return An int representing the number of winners in the "4+1" tier.
	 */
	public int getPrizeTier4_1winners() {return this.prizeTier4_1winners;}

	/**
	 * Get the shares of each winner in the "4+1" tier.
	 * @return An int representing the shares of each winner in the "4+1" tier.
	 */
	public double getPrizeTier4_1dividend() {return this.prizeTier4_1dividend;}

	/**
	 * Get the number of winners in the "4" tier.
	 * @return An int representing the number of winners in the "4" tier.
	 */
	public int getPrizeTier4winners() {return this.prizeTier4winners;}

	/**
	 * Get the shares of each winner in the "4" tier.
	 * @return An int representing the shares of each winner in the "4" tier.
	 */
	public double getPrizeTier4dividend() {return this.prizeTier4dividend;}

	/**
	 * Get the number of winners in the "3+1" tier.
	 * @return An int representing the number of winners in the "3+1" tier.
	 */
	public int getPrizeTier3_1winners() {return this.prizeTier3_1winners;}

	/**
	 * Get the shares of each winner in the "3+1" tier.
	 * @return An int representing the shares of each winner in the "3+1" tier.
	 */
	public double getPrizeTier3_1dividend() {return this.prizeTier3_1dividend;}

	/**
	 * Get the number of winners in the "3" tier.
	 * @return An int representing the number of winners in the "3" tier.
	 */
	public int getPrizeTier3winners() {return this.prizeTier3winners;}

	/**
	 * Get the shares of each winner in the "3" tier.
	 * @return An int representing the shares of each winner in the "3" tier.
	 */
	public double getPrizeTier3dividend() {return this.prizeTier3dividend;}

	/**
	 * Get the number of winners in the "2+1" tier.
	 * @return An int representing the number of winners in the "2+1" tier.
	 */
	public int getPrizeTier2_1winners() {return this.prizeTier2_1winners;}

	/**
	 * Get the shares of each winner in the "2+1" tier.
	 * @return An int representing the shares of each winner in the "2+1" tier.
	 */
	public double getPrizeTier2_1dividend() {return this.prizeTier2_1dividend;}

	/**
	 * Get the number of winners in the "1+1" tier.
	 * @return An int representing the number of winners in the "1+1" tier.
	 */
	public int getPrizeTier1_1winners() {return this.prizeTier1_1winners;}

	/**
	 * Get the shares of each winner in the "1+1" tier.
	 * @return An int representing the shares of each winner in the "1+1" tier.
	 */
	public double getPrizeTier1_1dividend() {return this.prizeTier1_1dividend;}
}