package application;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


//コントローラ
public class SampleController extends Main implements Initializable, NativeKeyListener,NativeMouseListener{

	@FXML
	private RadioButton radioInput;

	@FXML
	private RadioButton radioPickup;

	@FXML
	private ToggleGroup mode;

	@FXML
	private ToggleGroup nary;

	@FXML
	private RadioButton hex;

	@FXML
	private RadioButton decimal;

	@FXML
	private RadioButton binary;

	@FXML
	private TextField red;

	@FXML
	private TextField green;

	@FXML
	private TextField blue;

	@FXML
	private TextField opacity;

	@FXML
	private Rectangle rectangle;

	@FXML
	private Button changeBtn;

	@FXML
	private Button mouseClicked;

	private boolean shiftPressed = false;

	@Override
    public void initialize(URL location, ResourceBundle resources) {

		// ログを抑制
		LogManager.getLogManager().reset();
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);

		GlobalScreen.setEventDispatcher(new JavaFxDispatchService());
		// JNativeHookフックしてなかったらフック
        if (!GlobalScreen.isNativeHookRegistered()) {
            try {
            	//GlobalScreen.setEventDispatcher(new JavaFxDispatchService());
                GlobalScreen.registerNativeHook();

            } catch (NativeHookException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        // ハンドラを登録
        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);

    }

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		if (arg0.getKeyCode() == NativeKeyEvent.VC_SHIFT ) {
			shiftPressed = false;
			//System.out.println(CTLPressed);
		}

	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		if (arg0.getKeyCode() == NativeKeyEvent.VC_SHIFT ) {
			shiftPressed = true;
			//System.out.println(CTLPressed);
		}
	}

	//マウスがクリックされた時のイベント
	@Override
	public void nativeMouseClicked(NativeMouseEvent arg0) {

		if(radioPickup.isSelected() && shiftPressed) {
			int pointX = arg0.getX();
			int pointY = arg0.getY();

			Robot robot = null;
			try {
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			java.awt.Color c = robot.getPixelColor(pointX, pointY);

			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			double a = c.getAlpha() / 255.0;
/*
			ArrayList<Integer> al = new ArrayList<>();
			al.add(rr);
			al.add(gg);
			al.add(bb);
			Collections.sort(al);
			int small = al.get(0);
			double a = (255.0 - small) / 255.0;
			int r = (int) Math.round((rr - small) / a);
			int g = (int) Math.round((gg - small) / a);
			int b = (int) Math.round((bb - small) / a);
			System.out.println("rr= " + rr + " gg= "+ gg + " bb= " + bb + " small= " + small + " aa= " + aa);
*/
			System.out.println("r= " + r + " g= "+ g + " b= " + b + " alpha= " + a);

			Color color = Color.rgb(r,g,b,a);
			rectangle.setFill(color);
			opacity.setText(Double.toString(a));

			if(decimal.isSelected()) {
				red.setText(Integer.toString(r));
				green.setText(Integer.toString(g));
				blue.setText(Integer.toString(b));
			}
			if(hex.isSelected()) {
				red.setText(Integer.toHexString(r));
				green.setText(Integer.toHexString(g));
				blue.setText(Integer.toHexString(b));
			}
			if(binary.isSelected()) {
				red.setText(Integer.toBinaryString(r));
				green.setText(Integer.toBinaryString(g));
				blue.setText(Integer.toBinaryString(b));
			}
		}
	}
	//inputラジオボタンが押下された時のイベントハンドラ
    @FXML
    private void handleRadioInput(ActionEvent event) {
    	decimal.setSelected(true);
    	red.setEditable(true);
    	green.setEditable(true);
    	blue.setEditable(true);
    	opacity.setEditable(true);
    	changeBtn.setDisable(false);
    }

    //pickupラジオボタンが押下された時のイベントハンドラ
    @FXML
    private void handleRadioPickup(ActionEvent event) {
    	red.setEditable(false);
    	green.setEditable(false);
    	blue.setEditable(false);
    	opacity.setEditable(false);
    	changeBtn.setDisable(true);
    }

  //hexラジオボタンが押下された時のイベントハンドラ
    @FXML
    private void handleRadioHex(ActionEvent event) {
    	String stringRed =  red.getText();
    	String stringGreen = green.getText();
    	String stringBlue = blue.getText();

    	//10進数
    	if(stringRed.matches("[0-9]{1,3}") &&
    			stringGreen.matches("[0-9]{1,3}") &&
    			stringBlue.matches("[0-9]{1,3}")) {
    		red.setText(Integer.toHexString(Integer.parseInt(stringRed)));
    		green.setText(Integer.toHexString(Integer.parseInt(stringGreen)));
    		blue.setText(Integer.toHexString(Integer.parseInt(stringBlue)));
    	}
    	//2進数
    	if(stringRed.matches("[0-1]{1,8}") &&
    			stringGreen.matches("[0-1]{1,8}") &&
    			stringBlue.matches("[0-1]{1,8}")) {
    		red.setText(Integer.toHexString(Integer.parseInt(stringRed,2)));
    		green.setText(Integer.toHexString(Integer.parseInt(stringGreen,2)));
    		blue.setText(Integer.toHexString(Integer.parseInt(stringBlue,2)));
    	}
    }

    //decimalラジオボタンが押下された時のイベントハンドラ
    @FXML
    private void handleRadioDecimal(ActionEvent event) {
    	String stringRed =  red.getText();
    	String stringGreen = green.getText();
    	String stringBlue = blue.getText();

    	//16進数
    	if(stringRed.matches("[0-9a-fA-F]{1,2}") &&
    			stringGreen.matches("[0-9a-fA-F]{1,2}") &&
    			stringBlue.matches("[0-9a-fA-F]{1,2}")) {
    		red.setText(Integer.toString(Integer.parseInt(stringRed,16)));
    		green.setText(Integer.toString(Integer.parseInt(stringGreen,16)));
    		blue.setText(Integer.toString(Integer.parseInt(stringBlue,16)));
    	}

    	//2進数
    	if(stringRed.matches("[0-1]{1,8}") &&
    			stringGreen.matches("[0-1]{1,8}") &&
    			stringBlue.matches("[0-1]{1,8}")) {
    		red.setText(Integer.toString(Integer.parseInt(stringRed,2)));
    		green.setText(Integer.toString(Integer.parseInt(stringGreen,2)));
    		blue.setText(Integer.toString(Integer.parseInt(stringBlue,2)));
    	}
    }

    //binaryラジオボタンが押下された時のイベントハンドラ
    @FXML
    private void handleRadioBinary(ActionEvent event) {
    	String stringRed =  red.getText();
    	String stringGreen = green.getText();
    	String stringBlue = blue.getText();

    	//16進数
    	if(stringRed.matches("[0-9a-fA-F]{1,2}") &&
    			stringGreen.matches("[0-9a-fA-F]{1,2}") &&
    			stringBlue.matches("[0-9a-fA-F]{1,2}")) {
    		red.setText(Integer.toBinaryString(Integer.parseInt(stringRed,16)));
    		green.setText(Integer.toBinaryString(Integer.parseInt(stringGreen,16)));
    		blue.setText(Integer.toBinaryString(Integer.parseInt(stringBlue,16)));
    	}

    	//10進数
    	if(stringRed.matches("[0-9]{1,3}") &&
    			stringGreen.matches("[0-9]{1,3}") &&
    			stringBlue.matches("[0-9]{1,3}")) {
    		red.setText(Integer.toBinaryString(Integer.parseInt(stringRed)));
    		green.setText(Integer.toBinaryString(Integer.parseInt(stringGreen)));
    		blue.setText(Integer.toBinaryString(Integer.parseInt(stringBlue)));
    	}
    }

    //changeボタンが押下された時のイベントハンドラ
    @FXML
    private void handleChangeBtn(ActionEvent event) {
    	try {
	    	int redColor ;
	    	int greenColor ;
	    	int blueColor ;
	    	double opacityColor = Double.parseDouble(opacity.getText());

	    	if(hex.isSelected()) {
	    		redColor = Integer.parseInt(red.getText(),16);
	    		greenColor = Integer.parseInt(green.getText(),16);
	    		blueColor = Integer.parseInt(blue.getText(),16);
	    	}

	    	else if(binary.isSelected()) {
	    		redColor = Integer.parseInt(red.getText(),2);
	    		greenColor = Integer.parseInt(green.getText(),2);
	    		blueColor = Integer.parseInt(blue.getText(),2);
	    	}

	    	else {
	    		redColor = Integer.parseInt(red.getText());
	    		greenColor = Integer.parseInt(green.getText());
	    		blueColor = Integer.parseInt(blue.getText());
	    	}

	    	Color recColor = Color.rgb(redColor,greenColor,blueColor,opacityColor);
	    	rectangle.setFill(recColor);
    	} catch(Exception e){
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setTitle("Error");
            alert.getDialogPane().setHeaderText( "Input Error" );
            //alert.getDialogPane().setContentText( "Input Error" );
            alert.showAndWait();
    	}
    }

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}


	@Override
	public void nativeMousePressed(NativeMouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void nativeMouseReleased(NativeMouseEvent arg0) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
