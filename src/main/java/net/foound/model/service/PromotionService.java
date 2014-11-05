package net.foound.model.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.foound.exception.HttpException;
import net.foound.model.entity.Establishment;
import net.foound.model.entity.Promotion;
import net.foound.model.repository.PromotionRepository;
import net.foound.util.Constants;
import net.foound.util.ControllerHelper;
import net.foound.util.ImageHelper;
import net.foound.util.MediaTypeHelper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PromotionService
{
	private static final String AVATAR_UPLOAD_PATH = "/uploads/promotion/avatar/";
	private static final int AVATAR_UPLOAD_SIZE = 150;
	
	@Autowired
	private PromotionRepository promotionRepository;
	
	@Autowired
	private EstablishmentService establishmentService;
	
	@Transactional(readOnly = true)
	public List<Promotion> findAll()
	{
		return promotionRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Promotion findOne(Long id)
	{
		return promotionRepository.findOne(id);
	}
	
	@Transactional
	// must not create or update a(n) new/old establishment
	public Promotion saveAsInsert(Promotion promotion)
	{
		if(promotion.getCreation() != null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_CREATION_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		promotion.setCreation(new Date());
		
		validateAsInsert(promotion);
		
		return promotionRepository.save(promotion);
	}
	
	@Transactional
	private Promotion save(Promotion promotion)
	{
		return promotionRepository.save(promotion);
	}
	
	@Transactional
	public Promotion activate(Long id, boolean active)
	{
		Promotion promotion = promotionRepository.findOne(id);
		
		if(promotion != null)
		{
			promotion.setActive(active);
			
			return save(promotion);
		}
		else
		{
			throw new HttpException(Constants.TEXT_PROMOTION_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	public Promotion changeAvatar(Long id, MultipartFile avatarMultipartFile, HttpServletRequest request)
	{
		Promotion foundPromotion = promotionRepository.findOne(id);
		
		if(foundPromotion != null)
		{
			return changeAvatar(foundPromotion, avatarMultipartFile, request);
		}
		else
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Promotion changeAvatar(Promotion promotion, MultipartFile avatarMultipartFile, HttpServletRequest request)
	{
		validateAsChangeAvatar(avatarMultipartFile);
		
		try
		{
			InputStream is = avatarMultipartFile.getInputStream();
			BufferedImage originalImage = ImageIO.read(is);
			
			BufferedImage resizedImage = ImageHelper.createResizedCopyProportionally(originalImage, AVATAR_UPLOAD_SIZE, true);
			ByteArrayOutputStream resizedImageByteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(resizedImage, MediaTypeHelper.getExtension(avatarMultipartFile.getContentType()), resizedImageByteArrayOutputStream);
			
			String avatar = ControllerHelper.fileUpload(AVATAR_UPLOAD_PATH + avatarMultipartFile.getOriginalFilename(), resizedImageByteArrayOutputStream.toByteArray(), request);
			
			promotion.setAvatar(avatar);
			
			return save(promotion);
		}
		catch(NoSuchAlgorithmException | IOException exception)
		{
			throw new HttpException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public void validateAsChangeAvatar(MultipartFile avatarMultipartFile)
	{
		if(!avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_JPEG) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_JPG) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_JPE) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_GIF) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_PNG) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_BMP))
		{
			throw new HttpException(Constants.TEXT_AVATAR_MIME_TYPE_NOT_SUPPORTED, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	public void validateAsInsert(Promotion promotion)
	{
		validateIgnoringId(promotion);
		
		if(promotion.getId() != null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Promotion foundPromotionByEstablishmentIdAndTitle = promotionRepository.findByEstablishmentIdAndTitle(promotion.getEstablishment().getId(), promotion.getTitle());
		
		if(foundPromotionByEstablishmentIdAndTitle != null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_ESTABLISHMENT_TITLE_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	private void validateIgnoringId(Promotion promotion)
	{
		if(promotion == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getEstablishment() == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_ESTABLISHMENT_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getEstablishment().getId() == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_ESTABLISHMENT_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Establishment foundEstablishment = establishmentService.findOne(promotion.getEstablishment().getId());
		
		if(foundEstablishment == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_ESTABLISHMENT_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		
		promotion.setEstablishment(foundEstablishment);
		
		if(StringUtils.isEmpty(promotion.getTitle()))
		{
			throw new HttpException(Constants.TEXT_PROMOTION_TITLE_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getTitle().length() > 40)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_TITLE_MUST_NOT_BE_BIGGER_THAN_40_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getLimitScore() == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_LIMIT_SCORE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getLimitScore() <= 0)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_LIMIT_SCORE_MUST_BE_BIGGER_THAN_0, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getCreation() == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_CREATION_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(promotion.getActive() == null)
		{
			throw new HttpException(Constants.TEXT_PROMOTION_ACTIVE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
