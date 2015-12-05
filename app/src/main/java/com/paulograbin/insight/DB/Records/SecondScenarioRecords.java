package com.paulograbin.insight.DB.Records;

import android.content.Context;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Path;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;

/**
 * Created by paulograbin on 02/12/15.
 */
public class SecondScenarioRecords extends AbstractRecords {

    @Override
    public void insertRecords(Context context) {
        PlaceProvider mPlaceProvider = new PlaceProvider(context);
        BeaconProvider mBeaconProvider = new BeaconProvider(context);
        PlaceBeaconProvider mPlaceBeaconProvider = new PlaceBeaconProvider(context);
        PathProvider mPathProvider = new PathProvider(context);

        /*
         *  Places
         */
        Place pMesaEduardo = new Place("Posto de trabalho", "", "Vire a esquerda e siga até o corredor do setor", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796634, -51.147999);
        Place pCorretor1B = new Place("Corredor Setor 1B", "", "Vire a direita e siga reto até a porta de entrada do setor", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796648, -51.148182);
        Place pPortaSetor1B = new Place("Porta Setor 1B", "", "Ande a frente até passar pela porta automática", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796493, -51.148570);
        Place pBanheiro = new Place("Banheiro", "", "Saia do banheiro e siga reto até o corredor", Place.NOT_FAVORITE, Place.FINAL_DESTINATION, -29.796493, -51.148570);
        Place pCorredorSalasReuniao = new Place("Corredor salas de reunião", "", "Siga a frente até chegar a sala", Place.NOT_FAVORITE, Place.NO_DESTINATION, -29.796577, -51.148575);
        Place pSalaReuniao = new Place("Sala de reunião", "", "", Place.FAVORITE, Place.FINAL_DESTINATION, -29.796493, -51.148570);


        pMesaEduardo.setId(mPlaceProvider.insert(pMesaEduardo));
        pCorretor1B.setId(mPlaceProvider.insert(pCorretor1B));
        pPortaSetor1B.setId(mPlaceProvider.insert(pPortaSetor1B));
        pBanheiro.setId(mPlaceProvider.insert(pBanheiro));
        pCorredorSalasReuniao.setId(mPlaceProvider.insert(pCorredorSalasReuniao));
        pSalaReuniao.setId(mPlaceProvider.insert(pSalaReuniao));


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


        bBranco1.setId(mBeaconProvider.insert(bBranco1));
        bAmarelo.setId(mBeaconProvider.insert(bAmarelo));
        bAzul.setId(mBeaconProvider.insert(bAzul));
        bPreto.setId(mBeaconProvider.insert(bPreto));
        bBranco2.setId(mBeaconProvider.insert(bBranco2));
        b6.setId(mBeaconProvider.insert(b6));


        /*
         *  PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(pMesaEduardo.getId(), bBranco1.getId(), bBranco1.getUUID());
        PlaceBeacon pb2 = new PlaceBeacon(pCorretor1B.getId(), bAmarelo.getId(), bAmarelo.getUUID());
        PlaceBeacon pb3 = new PlaceBeacon(pPortaSetor1B.getId(), bAzul.getId(), bAzul.getUUID());
        PlaceBeacon pb4 = new PlaceBeacon(pBanheiro.getId(), bPreto.getId(), bPreto.getUUID());
        PlaceBeacon pb5 = new PlaceBeacon(pCorredorSalasReuniao.getId(), bBranco2.getId(), bBranco2.getUUID());
        PlaceBeacon pb6 = new PlaceBeacon(pSalaReuniao.getId(), b6.getId(), b6.getUUID());

        pb1.setId(mPlaceBeaconProvider.insert(pb1));
        pb2.setId(mPlaceBeaconProvider.insert(pb2));
        pb3.setId(mPlaceBeaconProvider.insert(pb3));
        pb4.setId(mPlaceBeaconProvider.insert(pb4));
        pb5.setId(mPlaceBeaconProvider.insert(pb5));
        pb6.setId(mPlaceBeaconProvider.insert(pb6));


        /*
         *  Path
         */
        Path ph0 = new Path(pMesaEduardo.getId(), pCorretor1B.getId(), 1);     // 10   20
        Path ph1 = new Path(pCorretor1B.getId(), pPortaSetor1B.getId(), 1);     // 10   20
        Path ph3 = new Path(pPortaSetor1B.getId(), pBanheiro.getId(), 1); // 10   30
        Path ph2 = new Path(pBanheiro.getId(), pCorredorSalasReuniao.getId(), 1);         // 20   40
        Path ph4 = new Path(pCorredorSalasReuniao.getId(), pSalaReuniao.getId(), 1);

        ph0.setId(mPathProvider.insert(ph0));
        ph1.setId(mPathProvider.insert(ph1));
        ph2.setId(mPathProvider.insert(ph2));
        ph3.setId(mPathProvider.insert(ph3));
        ph4.setId(mPathProvider.insert(ph4));
    }
}
