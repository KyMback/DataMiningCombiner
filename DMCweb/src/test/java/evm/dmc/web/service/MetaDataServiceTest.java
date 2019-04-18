package evm.dmc.web.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import evm.dmc.api.model.ProjectModel;
import evm.dmc.api.model.data.DataAttribute;
import evm.dmc.api.model.data.DataStorageModel;
import evm.dmc.api.model.data.MetaData;
import evm.dmc.api.model.datapreview.DataPreview;
import evm.dmc.core.api.AttributeType;
import evm.dmc.core.api.back.data.DataSrcDstType;
import evm.dmc.web.controllers.project.utils.DatasetTestUtils;
import evm.dmc.web.exceptions.ProjectNotFoundException;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
//@SpringBootTest
//@DataJpaTest
//@ActiveProfiles({"test", "devH2"})
//@Rollback
//@ComponentScan( basePackages = { "evm.dmc.web", "evm.dmc.core", "evm.dmc.service", "evm.dmc.model"})
@Slf4j
public class MetaDataServiceTest extends ServiceTest {
    @Autowired
    MetaDataService metaDataService;

    @Autowired
    ProjectService projectService;

    ProjectModel project;

    DataStorageModel dataStorage;

    MetaData metaData;

    static Stream<String> dataStream;

    int featuresCount = 10;
    String defaultHeader = "h0, h1, h2, h3, h4, h5, h6, h7, h8, h9";
    String generatedHeader = "0,1,2,3,4,5,6,7,8,9";

    Random random = new Random();

    private Supplier<String> randomSupplier = () -> {
        StringJoiner joiner = new StringJoiner(", ", "", "");
        for (int i = 0; i < featuresCount; i++) {
            joiner.add(String.valueOf(random.nextInt()));
        }
        return joiner.toString();
    };

    private Supplier<String> orderSupplier = () -> {
        StringJoiner joiner = new StringJoiner(", ", "", "");
        for (int i = 0; i < featuresCount; i++) {
            joiner.add(String.valueOf(i));
        }
        return joiner.toString();
    };

    @Before
    public void init() {
        super.init();

        project = projectService.getByName("test0").findFirst().get();
        assertNotNull(project);

        dataStorage = MetaDataService.newDataStorageModel(Paths.get("/test/"), Paths.get("/test/path"), DataSrcDstType.LOCAL_FS,
                DataStorageModel.DEFAULT_DELIMITER, true);
        assertNotNull(dataStorage);

        metaData = MetaDataService.newMetaData("file0", "description", dataStorage);
        assertNotNull(metaData);


        dataStream = Stream.generate(randomSupplier);

    }


    @Test
    public final void testPersistMetadata() {
        assertNull(metaData.getId());
        metaData = metaDataService.persistMetadata(metaData, project);
        assertNotNull(metaData.getId());

        assertThat(project.getDataSources(), hasItem(metaData));
        assertThat(metaData.getProject(), equalTo(project));
    }

    @Test
    public final void testPersistPreviewWithoutHeader() {
        assertNull(metaData.getId());
        int previewSize = 7;
        List<String> data = dataStream.limit(previewSize).collect(Collectors.toList());

        metaData.getStorage().setHasHeader(false);
        metaData = metaDataService.persistMetadata(metaData, project);

        DataPreview preview = metaDataService.createPreview(metaData, data);

        assertNotNull(preview);
        log.debug("Generated header: {}", preview.getHeader());
        log.debug("Generated Data: {}", data);
        log.debug("Preview       : {}", preview.getData());
        assertThat(preview.getData(), hasItems(data.get(0)));
        assertThat(preview.getData().size(), equalTo(previewSize));
        assertThat(preview.getHeader(), equalTo(generatedHeader));
    }

    @Test
    public final void testPersistPreviewWithHeader() {
        assertNull(metaData.getId());
        int previewSize = 10;
        List<String> data = dataStream.limit(previewSize).collect(Collectors.toList());
        data.add(0, defaultHeader);
        metaData = metaDataService.persistMetadata(metaData, project);

        DataPreview preview = metaDataService.createPreview(metaData, data);

        assertNotNull(preview);
        log.debug("Generated header: {}", preview.getHeader());
        log.debug("Generated Data: {}", data);
        log.debug("Preview       : {}", preview.getData());
        assertThat(preview.getData(), hasItems(data.get(0)));
        assertThat(preview.getData().size(), equalTo(previewSize));
        assertThat(preview.getHeader(), equalTo(defaultHeader));
    }

    @Test
    public final void testGetDataAttributes() {
        DataPreview preview = getPreviewData(Stream.generate(orderSupplier));
        assertThat(StringUtils.split(preview.getData().get(0), ",")[0], equalTo("0"));

        List<DataAttribute> attrs = metaDataService.getDataAttributes(preview);

        assertThat(attrs.get(0).getName(), equalTo("h0"));
        assertThat(attrs.get(7).getName(), equalTo("h7"));
        assertThat(attrs.get(4).getType(), equalTo(AttributeType.NUMERIC));
    }

    @Test
    public final void testPersistAttrubutes() {
        DataPreview preview = getPreviewData(Stream.generate(orderSupplier));
        List<DataAttribute> attrs = metaDataService.getDataAttributes(preview);
        metaData.setProject(project);

        assertTrue(metaData.getAttributes().isEmpty());

        metaData = metaDataService.persistAttributes(metaData, attrs);
        assertThat(metaData.getAttributes().size(), equalTo(attrs.size()));

        assertThat(metaData.getAttributes().keySet(), hasItems("h1", "h2", "h5", "h8", "h9"));

    }

    @Test
    public final void testGetActiveDelimiters() {
        String activeDelim = MetaDataService.getActiveDelimiters(defaultHeader, DataPreview.DEFAULT_DELIMITER);

        assertEquals(",", activeDelim);
    }

    @Test
    public final void testNewDataStorageModel() {
        DataStorageModel dataStorage = MetaDataService.newDataStorageModel(Paths.get("/test/"), Paths.get("/test/path"), DataSrcDstType.LOCAL_FS,
                DataStorageModel.DEFAULT_DELIMITER, true);
//		log.debug("URI: {}", dataStorage.getUri("/test/"));

        assertThat(dataStorage.getRelativePath(), equalTo("/path"));
    }

    @Test
    public final void testGetByProjectAndName() {
        ProjectModel project = projectService.getByName(DatasetTestUtils.TEST_PROJ_NAME)
                .findFirst().orElseThrow(() -> new ProjectNotFoundException(DatasetTestUtils.TEST_PROJ_NAME));

        Optional<MetaData> optMeta = metaDataService.getByProjectAndName(project, DatasetTestUtils.TEST_data_telecom);

        assertTrue(optMeta.isPresent());

        MetaData meta = optMeta.get();
        assertNotNull(meta.getAttributes());

        assertNotNull(meta.getAttributes().get("Churn"));

    }

    private DataPreview getPreviewData(Stream<String> stream) {
        int previewSize = 10;
        List<String> data = stream.limit(previewSize).collect(Collectors.toList());
        String delimiter = MetaDataService.getActiveDelimiters(data.get(0), DataStorageModel.DEFAULT_DELIMITER);
        return DataPreviewService.newDataPreview(defaultHeader, data, delimiter);
    }

}
