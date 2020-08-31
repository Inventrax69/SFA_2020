package com.inventrax_pepsi;

import android.app.Activity;
import com.robotium.recorder.executor.Executor;

@SuppressWarnings("rawtypes")
public class SplashScreenActivityExecutor extends Executor {

	@SuppressWarnings("unchecked")
	public SplashScreenActivityExecutor() throws Exception {
		super((Class<? extends Activity>) Class.forName("com.inventrax.activities.SplashScreenActivity"),  "com.inventrax.R.id.", new android.R.id(), false, false, "1457688455769");
	}

	public void setUp() throws Exception { 
		super.setUp();
	}
}
