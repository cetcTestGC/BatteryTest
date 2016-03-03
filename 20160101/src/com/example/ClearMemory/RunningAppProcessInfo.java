package com.example.ClearMemory;

public class RunningAppProcessInfo {
	
	/**
     * Constant for {@link #importance}: this is a persistent process.
     * Only used when reporting to process observers.
     * @hide
     */ 
    public static final int IMPORTANCE_PERSISTENT = 50; 

    /**
     * Constant for {@link #importance}: this process is running the
     * foreground UI.
     */ 
    public static final int IMPORTANCE_FOREGROUND = 100; 
       
    /**
     * Constant for {@link #importance}: this process is running something
     * that is actively visible to the user, though not in the immediate
     * foreground.
     */ 
    public static final int IMPORTANCE_VISIBLE = 200; 
       
    /**
     * Constant for {@link #importance}: this process is running something
     * that is considered to be actively perceptible to the user.  An
     * example would be an application performing background music playback.
     */ 
    public static final int IMPORTANCE_PERCEPTIBLE = 130; 
       
    /**
     * Constant for {@link #importance}: this process is running an
     * application that can not save its state, and thus can't be killed
     * while in the background.
     * @hide
     */ 
    public static final int IMPORTANCE_CANT_SAVE_STATE = 170; 
       
    /**
     * Constant for {@link #importance}: this process is contains services
     * that should remain running.
     */ 
    public static final int IMPORTANCE_SERVICE = 300; 
       
    /**
     * Constant for {@link #importance}: this process process contains
     * background code that is expendable.
     */ 
    public static final int IMPORTANCE_BACKGROUND = 400; 
       
    /**
     * Constant for {@link #importance}: this process is empty of any
     * actively running code.
     */ 
    public static final int IMPORTANCE_EMPTY = 500;

}
