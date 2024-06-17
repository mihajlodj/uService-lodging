package ftn.hotelsservice.services;

import ftn.hotelsservice.domain.dtos.*;
import ftn.hotelsservice.domain.entities.Lodge;
import ftn.hotelsservice.domain.entities.Photo;
import ftn.hotelsservice.domain.mappers.LodgeMapper;
import ftn.hotelsservice.exception.exceptions.NotFoundException;
import ftn.hotelsservice.repositories.LodgeRepository;
import ftn.hotelsservice.utils.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LodgeService {

    @Value("${upload.dir}")
    private String uploadDir;

    private final RestService restService;

    private final LodgeRepository lodgeRepository;

    public LodgeDto create(LodgeCreateRequest lodgeCreateRequest, List<MultipartFile> photos) {
        UserDto owner = getLoggedInUser();
        Lodge lodge = LodgeMapper.INSTANCE.fromCreateRequest(lodgeCreateRequest);
        lodge.setOwnerId(owner.getId());
        List<Photo> photoEntities = convertPhotos(photos);
        lodge.setPhotos(photoEntities);
        lodgeRepository.save(lodge);
        return LodgeMapper.INSTANCE.toDto(lodge);
    }

    private UserDto getLoggedInUser() {
        UUID id = AuthUtils.getLoggedUserId();
        UserDto user = restService.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User doesn't exist");
        }
        return user;
    }

    private List<Photo> convertPhotos(List<MultipartFile> photos) {
        List<Photo> photoEntities = new ArrayList<>();
        if (photos == null || photos.size() == 0) {
            return photoEntities;
        }
        for (MultipartFile photo : photos) {
            if (photo.getOriginalFilename() == "") {
                continue;
            }
            String url = savePhoto(photo);
            Photo p = new Photo();
            p.setUrl(url);
            photoEntities.add(p);
        }
        return photoEntities;
    }

    private String savePhoto(MultipartFile photo) {
        Path rootDir = Paths.get(uploadDir);
        File uploadDirectory = new File(rootDir.toUri());
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();       // Create the upload directory if it doesn't exist
        }

        // Generate a new file name to avoid collisions
        String originalFilename = photo.getOriginalFilename();
        String newFilename = UUID.randomUUID().toString() + "_" + originalFilename;
        Path targetFilePath = Paths.get(uploadDir, newFilename);

        // Save the file to the destination
        try {
            Files.copy(photo.getInputStream(), targetFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "/uploads/" + newFilename;
    }

    public LodgeDto getLodgeById(UUID lodgeId) {
        Lodge lodge = getLodge(lodgeId);
        return LodgeMapper.INSTANCE.toDto(lodge);
    }

    public LodgeInterserviceDto getLodgeByIdInterservice(UUID lodgeId) {
        Lodge lodge = getLodge(lodgeId);
        return LodgeMapper.INSTANCE.toInterserviceDto(lodge);
    }

    public List<LodgeDto> getAllLodges() {
        List<Lodge> lodges = getAllLodgesFromRepo();
        return LodgeMapper.INSTANCE.toDto(lodges);
    }

    public List<LodgeDto> searchLodges(LodgeSearchRequest searchRequest) {
        List<Lodge> lodges = new ArrayList<>();
        lodgeRepository.findAll(searchRequest.getPredicate()).forEach(lodges::add);
        return lodges.stream().map(LodgeMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    private Lodge getLodge(UUID lodgeId) {
        return lodgeRepository.findById(lodgeId).orElseThrow(() -> new NotFoundException("Lodge doesn't exist"));
    }

    private List<Lodge> getAllLodgesFromRepo() {
        return lodgeRepository.findAll();
    }

    public List<LodgeDto> getAllMineLodges() {
        UserDto owner = getLoggedInUser();
        List<Lodge> lodges = lodgeRepository.findByOwnerId(owner.getId());
        return LodgeMapper.INSTANCE.toDto(lodges);
    }

}
