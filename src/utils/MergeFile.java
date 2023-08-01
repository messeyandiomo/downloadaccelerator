package utils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

import gui.Observable;
import gui.Observer;

public class MergeFile extends Thread implements Observable {
	
	private String videoFileName = null;
	private String audioFileName = null;
	private String outputFileName = null;
	private Process commandProcess = null;
	private boolean complete = false;
	private long dataPercentage = 0;
	private ArrayList<Observer> listObserver = new ArrayList<Observer>();
	

	public MergeFile(DownloadDirs dir, DownloadProps videodownloadprops, DownloadProps audiodownloadprops) {
		// TODO Auto-generated constructor stub
		super();
		this.setVideoFileName(dir.getDestinationDir() + videodownloadprops.getFilename());
		this.setAudioFileName(dir.getDestinationDir() + audiodownloadprops.getFilename());
		this.setOutputFileName(dir.getDestinationDir() + videodownloadprops.getFilename() + ".merging." + videodownloadprops.getSubType());
		String[] cmd = {"ffmpeg", "-i", this.getAudioFileName(), "-i", this.getVideoFileName(), "-acodec", "copy", "-vcodec", "copy", this.getOutputFileName()};
		ProcessBuilder pb = new ProcessBuilder(cmd);
		try {
			this.setCommandProcess(pb.start());
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void run() {
		
		Scanner scan = new Scanner(commandProcess.getErrorStream());
		
		/** find duration **/
		Pattern durationPattern = Pattern.compile("(?<=Duration: )[^,]*");
		String duration = scan.findWithinHorizon(durationPattern, 0);
		
		if(duration != null) {
			String[] hms = duration.split(":");
			long totalSecs = (Integer.parseInt(hms[0]) * 3600) + (Integer.parseInt(hms[1]) * 60) + (Long.parseLong(hms[2]));
			
			/** find time as long as possible **/
			Pattern timePattern = Pattern.compile("(?<=time=)[\\\\d.]*");
			String matchTime = null;
			
			while((matchTime = scan.findWithinHorizon(timePattern, 0)) != null) {
				this.setDataPercentage(Long.parseLong(matchTime)/totalSecs);
				this.updateObserver();
			}
			
			this.setComplete(true);
			this.updateObserver();
			
			new File(this.getVideoFileName()).delete();
			new File(this.getAudioFileName()).delete();
			
			new File(this.getOutputFileName()).renameTo(new File(this.getVideoFileName()));
		}
		
	}

	@Override
	public void addObserver(Observer obs) {
		// TODO Auto-generated method stub
		this.listObserver.add(obs);
	}

	@Override
	public void updateObserver() {
		// TODO Auto-generated method stub
		boolean iscompleted = this.isCompleted();
		long datapercentage = this.getDataPercentage();
		for(Observer obs : this.listObserver)
			obs.update(iscompleted, false, datapercentage, false);
	}

	@Override
	public void delObserver() {
		// TODO Auto-generated method stub
		this.listObserver = new ArrayList<Observer>();
	}


	public String getVideoFileName() {
		return videoFileName;
	}


	public void setVideoFileName(String videoFileName) {
		this.videoFileName = videoFileName;
	}


	public String getAudioFileName() {
		return audioFileName;
	}


	public void setAudioFileName(String audioFileName) {
		this.audioFileName = audioFileName;
	}


	public String getOutputFileName() {
		return outputFileName;
	}


	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}


	public Process getCommandProcess() {
		return commandProcess;
	}


	public void setCommandProcess(Process commandProcess) {
		this.commandProcess = commandProcess;
	}


	public boolean isCompleted() {
		return complete;
	}


	public void setComplete(boolean complete) {
		this.complete = complete;
	}


	public long getDataPercentage() {
		return dataPercentage;
	}


	public void setDataPercentage(long dataPercentage) {
		this.dataPercentage += dataPercentage;
	}

}
