package com.sherwin.spectro.xrite;


import com.sherwin.spectro.xrite.CI52SWWXDSV4DLL;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface CI52SWWXDSV4DLL extends Library {
	CI52SWWXDSV4DLL INSTANCE = (CI52SWWXDSV4DLL) Native.loadLibrary(
        (Platform.isWindows() ? "Ci52+SWW" : "simpleDLLLinuxPort"), CI52SWWXDSV4DLL.class);
    // it's possible to check the platform on which program runs, for example purposes we assume that there's a linux port of the library (it's not attached to the downloadable project)
    String GetInterfaceVersion();
    boolean Connect();
    boolean Disconnect();
    boolean IsConnected();
    String GetCalibrationStandard();
    String GetSerialNum();
    int GetSpectralSetCount();
    int GetWavelengthCount();
    String GetSpectralSetName(int spectralset);
    int GetWavelengthValue(int w1);
    boolean Measure(); 
    float GetSpectralData(int spectralset, int w1);
    boolean IsDataReady(); 
    int GetCalStatus();
    String GetCalSteps();
    boolean CalibrateStep(String calstep);
    String GetCalMode();
    int GetCalProgress();
    boolean AbortCalibration();
    boolean ClearSamples();
    int GetSampleCount();
    boolean SetCurrentSample(int sample);
    float GetSampleData(int spectralset, int w1);
    String GetAvailableSettings();
    String GetSettingOptions(String theSetting);
    String GetOption(String theOption);
    boolean SetOption(String theSetting, String theOption);
    boolean ScanIsSupported();
    boolean ScanConfig(int patchCount, float patchWidth);
    boolean ScanStart();
    boolean ScanAbort();
    int ScanGetStatus();
    int ScanGetCount();
    String ScanGetData(int patchIndex, int dataSet);
    int GetLastErrorCode();
    String GetLastErrorString();
    String Execute(String cmdString);
    long ExecuteBinary(String inputBuffer, long inputLength, long timeout, 
    						String outputBuffer, long outputBufferSize);
    
}
