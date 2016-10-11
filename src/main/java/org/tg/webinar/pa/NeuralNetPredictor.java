package org.tg.webinar.pa;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
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
public class NeuralNetPredictor {
    static Logger logger = Logger.getLogger(RandomForestPredictor.class.getName());
    static PMML model = null;
    static NeuralNetworkEvaluator neuralNetEvaluator;

    static String opposite_team="England";
    static String chasing="true";
    static int batsmanScoreIn10Overs=8;
    static int indiaScoreIn10Overs=53;
    static int batsmanScoreIn20Overs=42;
    static int indiaScoreIn20Overs=110;
    static int batsmanScoreIn30Overs=80;
    static int indiaScoreIn30Overs=186;

    public static void main(String[] args) throws FileNotFoundException, JAXBException, SAXException {

        InputStream is = new FileInputStream(new File("./model/kohli_nnet_model.pmml"));
        InputSource source = new InputSource(is);
        SAXSource transformedSource = ImportFilter.apply(source);
        model = JAXBUtil.unmarshalPMML(transformedSource);
        neuralNetEvaluator = new NeuralNetworkEvaluator(model);

        List<FieldName> activeFields = neuralNetEvaluator.getActiveFields();
        List<FieldName> groupFields = neuralNetEvaluator.getGroupFields();
        List<FieldName> targetFields = neuralNetEvaluator.getTargetFields();

        logger.info("Active Fields: "+ activeFields);
        logger.info("Target Fields:" + targetFields);


        Map<FieldName, FieldValue> paramMap = new LinkedHashMap<FieldName, FieldValue>();

        addStringParam(paramMap,"opposite_team", opposite_team);
        addStringParam(paramMap, "chasing", chasing);
        addIntParam(paramMap,"batsmanScoreIn10Overs",batsmanScoreIn10Overs);
        addIntParam(paramMap,"indiaScoreIn10Overs",indiaScoreIn10Overs);
        addIntParam(paramMap,"batsmanScoreIn20Overs",batsmanScoreIn20Overs);
        addIntParam(paramMap,"indiaScoreIn20Overs",indiaScoreIn20Overs);
        addIntParam(paramMap,"batsmanScoreIn30Overs",batsmanScoreIn30Overs);
        addIntParam(paramMap, "indiaScoreIn30Overs", indiaScoreIn30Overs);

        Map<FieldName, ?> results = neuralNetEvaluator.evaluate(paramMap);
        FieldName targetName = neuralNetEvaluator.getTargetField();
        EntityProbabilityDistribution targetValue = (EntityProbabilityDistribution)results.get(targetName);
        System.out.println("[Neural Net Model]:> Probability Distribution is: "+targetValue);
        System.out.println("[Neural Net Model]:> Predicted Value is: "+targetValue.getResult());

    }

    private static void addIntParam(Map<FieldName, FieldValue> paramMap, String fieldNameStr, int fieldValueInt) {
        FieldName fn = new FieldName(fieldNameStr);
        FieldValue fv = neuralNetEvaluator.prepare(fn, fieldValueInt);
        paramMap.put(fn, fv);
    }

    private static void addStringParam(Map<FieldName, FieldValue> paramMap, String fieldNameStr, String fieldValueString) {
        FieldName fn = new FieldName(fieldNameStr);
        FieldValue fv = neuralNetEvaluator.prepare(fn, fieldValueString);
        paramMap.put(fn, fv);
    }
}
