package net.foound.model.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import net.foound.exception.HttpException;
import net.foound.model.entity.Establishment;
import net.foound.model.entity.Promotion;
import net.foound.model.entity.Speciality;
import net.foound.model.entity.User;
import net.foound.model.repository.EstablishmentRepository;
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
public class EstablishmentService
{
	private static final String AVATAR_UPLOAD_PATH = "/uploads/establishment/avatar/";
	private static final int AVATAR_UPLOAD_SIZE = 150;
	
	@Autowired
	private EstablishmentRepository establishmentRepository;
	
	@Autowired
	private SpecialityService specialityService;
	
	@Autowired
	private UserService userService;
	
	@Transactional(readOnly = true)
	public Establishment findOne(Long id)
	{
		return establishmentRepository.findOne(id);
	}
	
	@Transactional(readOnly = true)
	public List<Establishment> findAll()
	{
		return establishmentRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Establishment findByUserNickname(String userNickname)
	{
		return establishmentRepository.findByUserNickname(userNickname);
	}
	
	@Transactional(readOnly = true)
	public Establishment findByPromotionId(Long promotionId)
	{
		return establishmentRepository.findByPromotionId(promotionId);
	}
	
	@Transactional(readOnly = true)
	public List<Establishment> searchByName(String name)
	{
		return establishmentRepository.searchByName("%" + name + "%");
	}
	
	@Transactional
	public Establishment saveAsInsert(Establishment establishment)
	{
		validateAsInsert(establishment);
		
		return save(establishment);
	}
	
	@Transactional
	public Establishment saveAsUpdate(Establishment establishment)
	{
		validateAsUpdate(establishment);
		
		return save(establishment);
	}
	
	@Transactional
	public Establishment saveAsUpdate(Long id, Establishment establishment, boolean changeAvatar)
	{
		Establishment foundEstablishment = establishmentRepository.findOne(id);
		
		if(foundEstablishment != null)
		{
			List<Promotion> promotions = foundEstablishment.getPromotions();
			
			establishment.setId(id);
			
			if(!changeAvatar)
			{
				establishment.setAvatar(foundEstablishment.getAvatar());
			}
			
			establishment = saveAsUpdate(establishment);
			
			establishment.setPromotions(promotions);
			
			return establishment;
		}
		else
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Establishment save(Establishment establishment)
	{
		String phone = establishment.getPhone().replaceAll(Constants.TEXT_PATTERN_NON_NUMERIC, "");
		
		establishment.setPhone(phone);
		
		return establishmentRepository.save(establishment);
	}
	
	@Transactional(readOnly = true)
	public Establishment getFromRequest(HttpServletRequest request)
	{
		User user = userService.getFromRequest(request);
		
		if(user != null)
		{
			return establishmentRepository.findByUserId(user.getId());
		}
		else
		{
			return null;
		}
	}
	
	@Transactional
	public Establishment changeAvatar(Long id, MultipartFile avatarMultipartFile, HttpServletRequest request)
	{
		Establishment foundEstablishment = establishmentRepository.findOne(id);
		
		if(foundEstablishment != null)
		{
			return changeAvatar(foundEstablishment, avatarMultipartFile, request);
		}
		else
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_NOT_FOUND, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional
	private Establishment changeAvatar(Establishment establishment, MultipartFile avatarMultipartFile, HttpServletRequest request)
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
			
			establishment.setAvatar(avatar);
			
			return save(establishment);
		}
		catch(NoSuchAlgorithmException | IOException exception)
		{
			throw new HttpException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public Establishment setSpeciality(Establishment establishment)
	{
		Speciality speciality = specialityService.findByName(establishment.getSpeciality().getName());
		
		establishment.setSpeciality(speciality);
		
		return establishment;
	}
	
	public void validateAsChangeAvatar(MultipartFile avatarMultipartFile)
	{
		if(!avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_JPEG) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_JPG) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_JPE) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_GIF) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_PNG) && !avatarMultipartFile.getContentType().equalsIgnoreCase(Constants.TEXT_CONTENT_TYPE_IMAGE_BMP))
		{
			throw new HttpException(Constants.TEXT_AVATAR_MIME_TYPE_NOT_SUPPORTED, HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@Transactional(readOnly = true)
	// must create a new user
	// must not create a new speciality
	public void validateAsInsert(Establishment establishment)
	{
		validateIgnoringId(establishment);
		
		if(establishment.getId() != null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_ID_MUST_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Establishment foundEstablishmentByNameAddressLatitudeAndLongitude = establishmentRepository.findByNameAddressLatitudeAndLongitude(establishment.getName(), establishment.getAddress(), establishment.getLatitude(), establishment.getLongitude());
		
		if(foundEstablishmentByNameAddressLatitudeAndLongitude != null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_NAME_ADDRESS_LATITUDE_LONGITUDE_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Establishment foundEstablishmentByPhone = establishmentRepository.findByPhone(establishment.getPhone());
		
		if(foundEstablishmentByPhone != null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_PHONE_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
		}
		
		userService.validateAsInsert(establishment.getUser());
	}
	
	@Transactional(readOnly = true)
	// must not create a new user
	// can update an old user
	// must not create a new speciality
	public void validateAsUpdate(Establishment establishment)
	{
		validateIgnoringId(establishment);
		
		if(establishment.getId() == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_ID_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishmentRepository.findOne(establishment.getId()) == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
		
		Establishment foundEstablishmentByNameAddressLatitudeAndLongitude = establishmentRepository.findByNameAddressLatitudeAndLongitude(establishment.getName(), establishment.getAddress(), establishment.getLatitude(), establishment.getLongitude());
		
		if(foundEstablishmentByNameAddressLatitudeAndLongitude != null)
		{
			if(!foundEstablishmentByNameAddressLatitudeAndLongitude.getId().equals(establishment.getId()))
			{
				throw new HttpException(Constants.TEXT_ESTABLISHMENT_NAME_ADDRESS_LATITUDE_LONGITUDE_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		Establishment foundEstablishmentByPhone = establishmentRepository.findByPhone(establishment.getPhone());
		
		if(foundEstablishmentByPhone != null)
		{
			if(!foundEstablishmentByPhone.getId().equals(establishment.getId()))
			{
				throw new HttpException(Constants.TEXT_ESTABLISHMENT_PHONE_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
		
		userService.validateAsUpdate(establishment.getUser());
		
		Establishment foundEstablishmentByUserId = establishmentRepository.findByUserId(establishment.getUser().getId());
		
		if(foundEstablishmentByUserId != null)
		{
			if(!foundEstablishmentByUserId.getUser().getId().equals(establishment.getUser().getId()))
			{
				throw new HttpException(Constants.TEXT_ESTABLISHMENT_USER_IS_ALREADY_IN_USE, HttpStatus.NOT_ACCEPTABLE);
			}
		}
	}
	
	private void validateIgnoringId(Establishment establishment)
	{
		if(establishment == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getUser() == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_USER_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(establishment.getName()))
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_NAME_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getName().length() > 120)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_NAME_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(establishment.getAddress()))
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_ADDRESS_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getAddress().length() > 120)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_ADDRESS_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(establishment.getAbout()))
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_ABOUT_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getAbout().length() > 255)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_ABOUT_MUST_NOT_BE_BIGGER_THAN_255_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getLatitude() == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_LATITUDE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getLatitude().compareTo(new BigDecimal(-90.0f)) == -1 || establishment.getLatitude().compareTo(new BigDecimal(90.0f)) == 1)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_LATITUDE_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getLongitude() == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_LONGITUDE_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getLongitude().compareTo(new BigDecimal(-180.0f)) == -1 || establishment.getLongitude().compareTo(new BigDecimal(180.0f)) == 1)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_LONGITUDE_IS_INVALID, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(StringUtils.isEmpty(establishment.getPhone()))
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_PHONE_MUST_NOT_BE_EMPTY, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getPhone().length() > 15)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_PHONE_MUST_NOT_BE_BIGGER_THAN_15_CHARACTERS, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(establishment.getSpeciality() == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_SPECIALITY_MUST_NOT_BE_NULL, HttpStatus.NOT_ACCEPTABLE);
		}
		
		if(specialityService.findOne(establishment.getSpeciality().getId()) == null)
		{
			throw new HttpException(Constants.TEXT_ESTABLISHMENT_SPECIALITY_DOES_NOT_EXIST, HttpStatus.NOT_ACCEPTABLE);
		}
	}
}
