package com.apap.tutorial3.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial3.model.PilotModel;
import com.apap.tutorial3.service.PilotService;

@Controller
public class PilotController {
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping("/pilot/add")
	public String add(@RequestParam(value="id",required=true) String id,
			@RequestParam(value="licenseNumber",required=true) String licenseNumber,
			@RequestParam(value="name", required=true) String name,
			@RequestParam(value="flyHour", required=true) int flyHour) {
		PilotModel pilot = new PilotModel(id, licenseNumber, name, flyHour);
		pilotService.addPilot(pilot);
		return "add";
	}
	
	@RequestMapping("/pilot/view")
	public String view(@RequestParam("licenseNumber") String licenseNumber, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		
		model.addAttribute("pilot", archive);
		return "view-pilot";
	}
	
	@RequestMapping("/pilot/viewall")
	public String viewall(Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		
		model.addAttribute("pilotList", archive);
		return "viewall-pilot";
	}
	
	@RequestMapping(value = {"/pilot/view/license-number", "/pilot/view/license-number/{licenseNumber}"})
	public String viewPage(@PathVariable Optional<String> licenseNumber, Model model) {
		if(licenseNumber.isPresent()) {
			PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
			model.addAttribute("licenseNumber", licenseNumber.get());
			if(archive != null) {
				model.addAttribute("pilot", archive);
				return "view-pilot";
			}
			else {
				model.addAttribute("error", "pilot dengan nomor lisence "+licenseNumber.get()+" tidak ditemukan");
				return "errorMessage";
			}
		}else {
			model.addAttribute("error", "pilot dengan license number tidak ditemukan");
			return "errorMessage";
		}
	}
	
	@RequestMapping(value = {"/pilot/update/license-number/fly-hour/{flyHour}","/pilot/update/license-number/{licenseNumber}/fly-hour/{flyHour}"})
	public String viewPage(@PathVariable Optional<String> licenseNumber,@PathVariable Optional<Integer> flyHour, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(licenseNumber.get());
		if(licenseNumber.isPresent()&& flyHour.isPresent()) {
			model.addAttribute("licenseNumber", licenseNumber.get());
			model.addAttribute("flyHour", flyHour.get());
			if(archive != null) {
				archive.setFlyHour(flyHour.get());
				model.addAttribute("success", "Update Fly Hour berhasil. FlyHour pilot bernomor "+licenseNumber.get()+" berjumlah "+flyHour.get());
				return "successUpdate";
			}
			else {
				model.addAttribute("error", "pilot dengan nomor lisence "+licenseNumber.get()+" tidak ditemukan");
				return "errorMessage";
			}
		}else {
			model.addAttribute("error", "pilot dengan license number tidak ditemukan");
			return "errorMessage";
		}
	}
	@RequestMapping(value = {"/pilot/delete/id/{id}"})
	public String deletePilot(@PathVariable Optional<String> id, Model model) {
		List<PilotModel> archive = pilotService.getPilotList();
		if(id.isPresent()) {
			for (PilotModel airPilot: archive) {
				if(airPilot.getId().equalsIgnoreCase(id.get())) {
					archive.remove(airPilot);
					return "successDelete";
					}
				}
			model.addAttribute("error", "pilot dengan id number "+id.get()+" tidak ditemukan");
			return "errorMessage";
		}else{
			return "errorMessage";
		}
	}
}
