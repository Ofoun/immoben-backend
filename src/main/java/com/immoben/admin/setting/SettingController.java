package com.immoben.admin.setting;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.immoben.admin.AmazonS3Util;
import com.immoben.common.Constants;
import com.immoben.common.entity.Currency;
import com.immoben.common.entity.setting.Setting;


@Controller
public class SettingController {

	@Autowired
	private SettingService service;

	@Autowired
	private CurrencyRepository currencyRepo;

	@GetMapping("/settings")
	public String listAll (Model model) {
		List<Setting> listSettings = service.listAllSettings();
		List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		model.addAttribute("S3_BASE_URI", Constants.S3_BASE_URI);

		return "settings/settings";
	}


	@PostMapping("/settings/save_general")
	public String saveGeneralSettings (@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes ra) throws IOException {
		GeneralSettingBag settingBag = service.getGeneralSettings();

		saveSiteLogo(multipartFile, settingBag);
		saveCurrencySymbol(request, settingBag);

		updateSettingValuesFromForm(request, settingBag.list());

		ra.addFlashAttribute("message", "Les paramètres généraux ont été enregistrés avec succès.");

		return "redirect:/settings";
	}


	private void saveSiteLogo (MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);

			String uploadDir = "site-logo";
			AmazonS3Util.removeFolder(uploadDir);
			AmazonS3Util.uploadFile(uploadDir, fileName, multipartFile.getInputStream());
		}
	}


	private void saveCurrencySymbol (HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}


	private void updateSettingValuesFromForm (HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}

		service.saveAll(listSettings);
	}


	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSetttings (HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailServerSettings = service.getMailServerSettings();
		updateSettingValuesFromForm(request, mailServerSettings);

		ra.addFlashAttribute("message", "Les paramètres du serveur de messagerie ont été enregistrés avec succès.");

		return "redirect:/settings#mailServer";
	}


	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSetttings (HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> mailTemplateSettings = service.getMailTemplateSettings();
		updateSettingValuesFromForm(request, mailTemplateSettings);

		ra.addFlashAttribute("message", "Les paramètres du modèle d'e-mail ont été enregistrés avec succès.");

		return "redirect:/settings#mailTemplates";
	}


	@PostMapping("/settings/save_payment")
	public String savePaymentSetttings (HttpServletRequest request, RedirectAttributes ra) {
		List<Setting> paymentSettings = service.getPaymentSettings();
		updateSettingValuesFromForm(request, paymentSettings);

		ra.addFlashAttribute("message", "Les paramètres de paiement ont été enregistrés avec succès.");

		return "redirect:/settings#payment";
	}
}
