package com.paulograbin.insight.DB;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.DB.Table.TableBeacon;
import com.paulograbin.insight.DB.Table.TablePath;
import com.paulograbin.insight.DB.Table.TablePlace;
import com.paulograbin.insight.DB.Table.TablePlaceBeacon;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;

/**
 * Created by paulograbin on 30/06/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 29;
    private static final String DATABASE_NAME = "insight.db";
    private static DatabaseHelper mDatabaseHelper;
    private static Context context;
    private final String TAG = "Database";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DatabaseHelper.context = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (mDatabaseHelper == null)
            mDatabaseHelper = new DatabaseHelper(context);

        return mDatabaseHelper;
    }

    public void checkDatabase() {
        printToLog("Checking database...");

        BeaconProvider bp = new BeaconProvider(context);
        PlaceProvider pp = new PlaceProvider(context);
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);

        if ((bp.getCount() == 0) && (pp.getCount() == 0) && (pbp.getCount() == 0)) {
            insertStandardRecords();
        }
    }

    private void insertBeaconTestRecords() {
        PlaceProvider pp = new PlaceProvider(context);
        BeaconProvider bp = new BeaconProvider(context);
        PlaceBeaconProvider pbp = new PlaceBeaconProvider(context);

        /**
         *  PLACES
         */
        Place pAmarelo    = new Place("Amarelo", "Novo Hamburgo",    "", 0, 1, -29.685204, -51.140426);
        Place pAzul       = new Place("Azul", "São Leopoldo",        "", 0, 1, -29.772558, -51.151413);
        Place pPreto      = new Place("Preto", "Esteio",             "", 0, 1, -29.848991, -51.175000);
        Place pBrancoUm   = new Place("Branco Um", "Canoas",         "", 0, 1, -29.913290, -51.179120);
        Place pBrancoDois = new Place("Branco Dois", "Porto Alegre", "", 0, 1, -30.037008, -51.227528);

        pAmarelo.setId(pp.insert(pAmarelo));
        pAzul.setId(pp.insert(pAzul));
        pPreto.setId(pp.insert(pPreto));
        pBrancoUm.setId(pp.insert(pBrancoUm));
        pBrancoDois.setId(pp.insert(pBrancoDois));


        /**
         *  BEACONS
         */
        Beacon bAzul = new Beacon();
        bAzul.setUUID(BeaconProvider.BEACON_AZUL);

        Beacon bAmarelo = new Beacon();
        bAmarelo.setUUID(BeaconProvider.BEACON_AMARELO);

        Beacon bPreto = new Beacon();
        bPreto.setUUID(BeaconProvider.BEACON_PRETO);

        Beacon bBrancoUm = new Beacon();
        bBrancoUm.setUUID(BeaconProvider.BEACON_BRANCO_1);

        Beacon bBrancoDois =  new Beacon();
        bBrancoDois.setUUID(BeaconProvider.BEACON_BRANCO_2);

        bAzul.setId(bp.insert(bAzul));
        bAmarelo.setId(bp.insert(bAmarelo));
        bPreto.setId(bp.insert(bPreto));
        bBrancoUm.setId(bp.insert(bBrancoUm));
        bBrancoDois.setId(bp.insert(bBrancoDois));


        /**
         *  PLACEBEACONS
         */
        PlaceBeacon pbPreto = new PlaceBeacon(pPreto.getId(), bPreto.getId(), bPreto.getUUID());
        pbPreto.setId(pbp.insert(pbPreto));

        PlaceBeacon pbAmarelo = new PlaceBeacon(pAmarelo.getId(), bAmarelo.getId(), bAmarelo.getUUID());
        pbAmarelo.setId(pbp.insert(pbAmarelo));

        PlaceBeacon pbAzul = new PlaceBeacon(pAzul.getId(), bAzul.getId(), bAzul.getUUID());
        pbAzul.setId(pbp.insert(pbAzul));

        PlaceBeacon pbBrancoUm = new PlaceBeacon(pBrancoUm.getId(), bBrancoUm.getId(), bBrancoUm.getUUID());
        pbBrancoUm.setId(pbp.insert(pbBrancoUm));

        PlaceBeacon pbBrancoDois = new PlaceBeacon(pBrancoDois.getId(), bBrancoDois.getId(), bBrancoDois.getUUID());
        pbBrancoDois.setId(pbp.insert(pbBrancoDois));
    }


    private void firstScenarioRecords() {
        PlaceProvider placeProvider = new PlaceProvider(context);
        BeaconProvider beaconProvider = new BeaconProvider(context);
        PlaceBeaconProvider placeBeaconProvider = new PlaceBeaconProvider(context);
        PathProvider pathProvider = new PathProvider(context);

        /*
         *  Places
         */
        Place pEntrada = new Place("Entrada", "Porta de entrada", "Ande a frente até passar pela porta automática", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796493, -51.148570);
        Place pRecepcao = new Place("Recepção", "Saguão de recepção", "Dê dois passos a frente e vire a direita, siga até passar pelas catracas", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796493, -51.148570);
        Place pPrimeiroAndar = new Place("Primeiro Andar", "Acesso aos outros andares", "Vire a esquerda e ande até chegar a escada, e então suba. São dois lances com 10 degraus", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796577, -51.148575);
        Place pSegundoAndar = new Place("Segundo Andar", "Acesso aos outros andares", "Mensagem...", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796577, -51.148575);
        Place pEntradaSetor1B = new Place("Entrada do Setor 1B", "", "Mensagem de teste!", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796493, -51.148570);
        Place pCorretor1B = new Place("Corredor Setor 1B", "", "Mensagem de teste!", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796648, -51.148182);
        Place pMesaEduardo = new Place("Posto de trabalho", "", "Mensagem de teste!", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796634, -51.147999);

        pEntrada.setId(placeProvider.insert(pEntrada));
        pRecepcao.setId(placeProvider.insert(pRecepcao));
        pPrimeiroAndar.setId(placeProvider.insert(pPrimeiroAndar));
        pSegundoAndar.setId(placeProvider.insert(pSegundoAndar));
        pEntradaSetor1B.setId(placeProvider.insert(pEntradaSetor1B));
        pCorretor1B.setId(placeProvider.insert(pCorretor1B));
        pMesaEduardo.setId(placeProvider.insert(pMesaEduardo));

        /*
         *  Beacon
         */
        Beacon bBranco1 = new Beacon();
        Beacon bAmarelo = new Beacon();
        Beacon bAzul = new Beacon();
        Beacon bPreto = new Beacon();
        Beacon bBranco2 = new Beacon();
        Beacon b6 = new Beacon();

        b6.setUUID(BeaconProvider.MY_BEACON_UUID1);
        bBranco1.setUUID(BeaconProvider.MY_BEACON_UUID2);
        bAmarelo.setUUID(BeaconProvider.MY_BEACON_UUID3);
        bAzul.setUUID(BeaconProvider.MY_BEACON_UUID4);
        bPreto.setUUID(BeaconProvider.MY_BEACON_UUID5);
        bBranco2.setUUID(BeaconProvider.MY_BEACON_UUID6);


        bBranco1.setId(beaconProvider.insert(bBranco1));
        bAmarelo.setId(beaconProvider.insert(bAmarelo));
        bAzul.setId(beaconProvider.insert(bAzul));
        bPreto.setId(beaconProvider.insert(bPreto));
        bBranco2.setId(beaconProvider.insert(bBranco2));
        b6.setId(beaconProvider.insert(b6));


        /*
         *  PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(pRecepcao.getId(), bBranco1.getId(), bBranco1.getUUID());
        PlaceBeacon pb2 = new PlaceBeacon(pPrimeiroAndar.getId(), bAmarelo.getId(), bAmarelo.getUUID());
        PlaceBeacon pb3 = new PlaceBeacon(pSegundoAndar.getId(), bAzul.getId(), bAzul.getUUID());
        PlaceBeacon pb4 = new PlaceBeacon(pEntradaSetor1B.getId(), bPreto.getId(), bPreto.getUUID());
        PlaceBeacon pb5 = new PlaceBeacon(pCorretor1B.getId(), bBranco2.getId(), bBranco2.getUUID());
        PlaceBeacon pb6 = new PlaceBeacon(pEntrada.getId(), b6.getId(), b6.getUUID());

        pb1.setId(placeBeaconProvider.insert(pb1));
        pb2.setId(placeBeaconProvider.insert(pb2));
        pb3.setId(placeBeaconProvider.insert(pb3));
        pb4.setId(placeBeaconProvider.insert(pb4));
        pb5.setId(placeBeaconProvider.insert(pb5));
        pb6.setId(placeBeaconProvider.insert(pb6));


        /*
         *  Path
         */
        Path ph0 = new Path(pEntrada.getId(), pRecepcao.getId(), 1);     // 10   20
        Path ph1 = new Path(pRecepcao.getId(), pPrimeiroAndar.getId(), 1);     // 10   20
        Path ph3 = new Path(pPrimeiroAndar.getId(), pSegundoAndar.getId(), 1); // 10   30
        Path ph2 = new Path(pSegundoAndar.getId(), pCorretor1B.getId(), 1);         // 20   40
        Path ph4 = new Path(pCorretor1B.getId(), pMesaEduardo.getId(), 1);
        Path ph5 = new Path(pPrimeiroAndar.getId(), pSegundoAndar.getId(), 1);


        ph0.setId(pathProvider.insert(ph0));
        ph1.setId(pathProvider.insert(ph1));
        ph2.setId(pathProvider.insert(ph2));
        ph3.setId(pathProvider.insert(ph3));
        ph4.setId(pathProvider.insert(ph4));
        ph5.setId(pathProvider.insert(ph5));
    }

    public void insertStandardRecords() {
        Log.i("Database", "Inserting records...");

        firstScenarioRecords();
//        secondScenarioRecords();
        printToLog("Inserindo registros de teste...");
    }

    private void secondScenarioRecords() {
        PlaceProvider placeProvider = new PlaceProvider(context);
        BeaconProvider beaconProvider = new BeaconProvider(context);
        PlaceBeaconProvider placeBeaconProvider = new PlaceBeaconProvider(context);
        PathProvider pathProvider = new PathProvider(context);

        /*
         *  Places
         */
        Place pMesaEduardo = new Place("Posto de trabalho", "", "Vire a esquerda e siga até o corredor do setor", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796634, -51.147999);
        Place pCorretor1B = new Place("Corredor Setor 1B", "", "Vire a direita e siga reto até a porta de entrada do setor", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796648, -51.148182);
        Place pPortaSetor1B = new Place("Porta Setor 1B", "", "Ande a frente até passar pela porta automática", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796493, -51.148570);
        Place pBanheiro = new Place("Banheiro", "", "Saia do banheiro e siga reto até o corredor", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.796493, -51.148570);
        Place pCorredorSalasReuniao = new Place("Corredor salas de reunião", "", "Siga a frente até chegar a sala", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796577, -51.148575);
        Place pSalaReuniao = new Place("Sala de reunião", "", "", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796493, -51.148570);


        pMesaEduardo.setId(placeProvider.insert(pMesaEduardo));
        pCorretor1B.setId(placeProvider.insert(pCorretor1B));
        pPortaSetor1B.setId(placeProvider.insert(pPortaSetor1B));
        pBanheiro.setId(placeProvider.insert(pBanheiro));
        pCorredorSalasReuniao.setId(placeProvider.insert(pCorredorSalasReuniao));
        pSalaReuniao.setId(placeProvider.insert(pSalaReuniao));


        /*
         *  Beacon
         */
        Beacon bBranco1 = new Beacon();
        Beacon bAmarelo = new Beacon();
        Beacon bAzul = new Beacon();
        Beacon bPreto = new Beacon();
        Beacon bBranco2 = new Beacon();
        Beacon b6 = new Beacon();


        bBranco1.setUUID(BeaconProvider.MY_BEACON_UUID1);
        bAmarelo.setUUID(BeaconProvider.MY_BEACON_UUID2);
        bAzul.setUUID(BeaconProvider.MY_BEACON_UUID3);
        bPreto.setUUID(BeaconProvider.MY_BEACON_UUID4);
        bBranco2.setUUID(BeaconProvider.MY_BEACON_UUID5);
        b6.setUUID(BeaconProvider.MY_BEACON_UUID6);


        bBranco1.setId(beaconProvider.insert(bBranco1));
        bAmarelo.setId(beaconProvider.insert(bAmarelo));
        bAzul.setId(beaconProvider.insert(bAzul));
        bPreto.setId(beaconProvider.insert(bPreto));
        bBranco2.setId(beaconProvider.insert(bBranco2));
        b6.setId(beaconProvider.insert(b6));


        /*
         *  PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(pMesaEduardo.getId(), bBranco1.getId(), bBranco1.getUUID());
        PlaceBeacon pb2 = new PlaceBeacon(pCorretor1B.getId(), bAmarelo.getId(), bAmarelo.getUUID());
        PlaceBeacon pb3 = new PlaceBeacon(pPortaSetor1B.getId(), bAzul.getId(), bAzul.getUUID());
        PlaceBeacon pb4 = new PlaceBeacon(pBanheiro.getId(), bPreto.getId(), bPreto.getUUID());
        PlaceBeacon pb5 = new PlaceBeacon(pCorredorSalasReuniao.getId(), bBranco2.getId(), bBranco2.getUUID());
        PlaceBeacon pb6 = new PlaceBeacon(pSalaReuniao.getId(), b6.getId(), b6.getUUID());

        pb1.setId(placeBeaconProvider.insert(pb1));
        pb2.setId(placeBeaconProvider.insert(pb2));
        pb3.setId(placeBeaconProvider.insert(pb3));
        pb4.setId(placeBeaconProvider.insert(pb4));
        pb5.setId(placeBeaconProvider.insert(pb5));
        pb6.setId(placeBeaconProvider.insert(pb6));


        /*
         *  Path
         */
        Path ph0 = new Path(pMesaEduardo.getId(), pCorretor1B.getId(), 1);     // 10   20
        Path ph1 = new Path(pCorretor1B.getId(), pPortaSetor1B.getId(), 1);     // 10   20
        Path ph3 = new Path(pPortaSetor1B.getId(), pBanheiro.getId(), 1); // 10   30
        Path ph2 = new Path(pBanheiro.getId(), pCorredorSalasReuniao.getId(), 1);         // 20   40
        Path ph4 = new Path(pCorredorSalasReuniao.getId(), pSalaReuniao.getId(), 1);

        ph0.setId(pathProvider.insert(ph0));
        ph1.setId(pathProvider.insert(ph1));
        ph2.setId(pathProvider.insert(ph2));
        ph3.setId(pathProvider.insert(ph3));
        ph4.setId(pathProvider.insert(ph4));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        printToLog("Criando tabelas...");

        try {
            createTables(db);
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void createTables(SQLiteDatabase db) {
        printToLog(TableBeacon.TABLE_CREATE_COMMAND);
        db.execSQL(TableBeacon.TABLE_CREATE_COMMAND);

        printToLog(TablePlace.TABLE_CREATE_COMMAND);
        db.execSQL(TablePlace.TABLE_CREATE_COMMAND);

        printToLog(TablePlaceBeacon.TABLE_CREATE_COMMAND);
        db.execSQL(TablePlaceBeacon.TABLE_CREATE_COMMAND);

        printToLog(TablePath.TABLE_CREATE_COMMAND);
        db.execSQL(TablePath.TABLE_CREATE_COMMAND);
    }

    public void dropTables() {
        printToLog("Dropping all records from all tables");
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TablePlace.TABLE_NAME, String.valueOf(1), null);
        db.delete(TableBeacon.TABLE_NAME, String.valueOf(1), null);
        db.delete(TablePlaceBeacon.TABLE_NAME, String.valueOf(1), null);
        db.delete(TablePath.TABLE_NAME, String.valueOf(1), null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        printToLog("Upgrading - Dropping all records from all tables");

        db.execSQL("DROP TABLE IF EXISTS " + TableBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlace.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePlaceBeacon.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TablePath.TABLE_NAME);

        onCreate(db);
    }

    private void printToLog(String message) {
        Log.i(TAG, message);
    }
}