package com.gaurav.food_application.service.foodservice;

import com.gaurav.food_application.entity.FoodEntity;
import com.gaurav.food_application.io.FoodRequest;
import com.gaurav.food_application.io.FoodResponse;
import com.gaurav.food_application.repository.FoodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private S3Client s3Client;
    @Value("${aws.s3.bucketname}")
    private String bucketName;
    private final FoodRepository foodRepository;
    public FoodServiceImpl(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }
    @Override
    public String uploadFile(MultipartFile file) {
        // ✅ FIXED: Properly format filename (original name preserved with UUID prefix)
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            PutObjectResponse putObjectResponse = s3Client.putObject(
                    putObjectRequest, RequestBody.fromBytes(file.getBytes())
            );
            if (putObjectResponse.sdkHttpResponse().isSuccessful()){
                String url =  "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
                log.info("image url {}",url);
                return url;
            }
                // ✅ FIXED: Corrected URL format
             else
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Upload failed");
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public FoodResponse addFood(FoodRequest foodRequest, MultipartFile file) {
        FoodEntity foodEntity = convertToEntity(foodRequest);
        String url = uploadFile(file);
        log.info("image url {}",url);
        foodEntity.setImageURL(url);
        return convertToResponse(this.foodRepository.save(foodEntity));
    }

    @Override
    public List<FoodResponse> getFoodList() {
        List<FoodResponse> foodResponseList = this.foodRepository.findAll()
                .stream().map(this::convertToResponse).toList();
        if(foodResponseList.isEmpty())
                throw new RuntimeException("No food found");
        return foodResponseList;
    }

    @Override
    public FoodResponse getFood(String foodId) {
        FoodEntity foodEntity = this.foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("No food founded"));
        return convertToResponse(foodEntity);
    }

    @Override
    public boolean deleteFile(String fileName) {
        try {
            System.out.println("Attempting to delete file from S3...");
            System.out.println("Bucket: " + bucketName);
            System.out.println("FileName(Key): " + fileName);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            System.out.println("File deleted successfully from S3.");
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    @Override
    public void deleteFood(String foodId) {
        boolean existFood = this.foodRepository.existsById(foodId);
        if(!existFood)
            throw new RuntimeException("No food founded");
        FoodResponse foodResponse = this.getFood(foodId);
        String imageURL = foodResponse.getImageURL();
        String fileName = imageURL.substring(imageURL.lastIndexOf("/")+1);
        boolean isDeleted = this.deleteFile(fileName);
        if(isDeleted)
            this.foodRepository.deleteById(foodId);
    }

    private FoodEntity convertToEntity(FoodRequest foodRequest){
        return FoodEntity.builder()
                .name(foodRequest.getName())
                .description(foodRequest.getDescription())
                .category(foodRequest.getCategory())
                .price(foodRequest.getPrice())
                .build();
    }
    private FoodResponse convertToResponse(FoodEntity foodEntity){
        return FoodResponse.builder()
                .id(foodEntity.getId())
                .name(foodEntity.getName())
                .description(foodEntity.getDescription())
                .category(foodEntity.getCategory())
                .price(foodEntity.getPrice())
                .imageURL(foodEntity.getImageURL())
                .build();
    }
}
