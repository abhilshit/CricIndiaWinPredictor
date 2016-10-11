package org.tg.webinar.pa;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.MiningModel;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.MiningModelEvaluator;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ProbabilityDistribution;
import org.jpmml.model.ImportFilter;
import org.jpmml.model.JAXBUtil;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.transform.sax.SAXSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by asoni on 10/11/16.
 */
public class RandomForestPredictor {

    static Logger logger = Logger.getLogger(RandomForestPredictor.class.getName());
    static PMML model = null;
    static ModelEvaluator<MiningModel> modelEvaluator;

    static String opposite_team="England";
    static String chasing="true";
    static int batsmanScoreIn10Overs=8;
    static int indiaScoreIn10Overs=53;
    static int batsmanScoreIn20Overs=42;
    static int indiaScoreIn20Overs=110;
    static int batsmanScoreIn30Overs=80;
    static int indiaScoreIn30Overs=186;

    public static void main(String[] args) throws FileNotFoundException, JAXBException, SAXException {

        InputStream is = new FileInputStream(new File("./model/kohli_rf_model.pmml"));
        InputSource source = new InputSource(is);
        SAXSource transformedSource = ImportFilter.apply(source);
        model = JAXBUtil.unmarshalPMML(transformedSource);
        modelEvaluator = new MiningModelEvaluator(model);

        List<FieldName> activeFields = modelEvaluator.getActiveFields();
        List<FieldName> groupFields = modelEvaluator.getGroupFields();
        List<FieldName> targetFields = modelEvaluator.getTargetFields();

        logger.info("Active Fields: "+ activeFields);
        logger.info("Target Fields:" + targetFields);


        Map<FieldName, FieldValue> paramMap = new LinkedHashMap<FieldName, FieldValue>();

        addStringParam(paramMap,"opposite_team", opposite_team);
        addStringParam(paramMap,"chasing", chasing);
        addIntParam(paramMap,"batsmanScoreIn10Overs",batsmanScoreIn10Overs);
        addIntParam(paramMap,"indiaScoreIn10Overs",indiaScoreIn10Overs);
        addIntParam(paramMap,"batsmanScoreIn20Overs",batsmanScoreIn20Overs);
        addIntParam(paramMap,"indiaScoreIn20Overs",indiaScoreIn20Overs);
        addIntParam(paramMap,"batsmanScoreIn30Overs",batsmanScoreIn30Overs);
        addIntParam(paramMap,"indiaScoreIn30Overs",indiaScoreIn30Overs);

        Map<FieldName, ?> results = modelEvaluator.evaluate(paramMap);
        FieldName targetName = modelEvaluator.getTargetField();
        ProbabilityDistribution targetValue = (ProbabilityDistribution)results.get(targetName);
        System.out.println("[RF Model]> Predicted Value is: "+targetValue);
        System.out.println("[RF Model]> Predicted Value is: "+targetValue.getResult());

    }

    private static void addIntParam(Map<FieldName, FieldValue> paramMap, String fieldNameStr, int fieldValueInt) {
        FieldName fn = new FieldName(fieldNameStr);
        FieldValue fv = modelEvaluator.prepare(fn, fieldValueInt);
        paramMap.put(fn, fv);
    }

    private static void addStringParam(Map<FieldName, FieldValue> paramMap, String fieldNameStr, String fieldValueString) {
        FieldName fn = new FieldName(fieldNameStr);
        FieldValue fv = modelEvaluator.prepare(fn, fieldValueString);
        paramMap.put(fn, fv);
    }
}
