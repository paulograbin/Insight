package com.paulograbin.insight.DB.Records;

import android.content.Context;

import com.paulograbin.insight.Builders.PlaceBuilder;
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
public class DemoRecords extends AbstractRecords {

    @Override
    public void insertRecords(Context context) {
        PlaceProvider mPlaceProvider = new PlaceProvider(context);
        BeaconProvider mBeaconProvider = new BeaconProvider(context);
        PlaceBeaconProvider mPlaceBeaconProvider = new PlaceBeaconProvider(context);
        PathProvider mPathProvider = new PathProvider(context);

        /*
         *  Places
         */
        Place pRecepcao = new PlaceBuilder()
                .withName("Recepção")
                .withMessage("Vire a direita e siga reto até o corredor")
                .withDescription("Saguão de recepção")
                .setDestination()
                .setFavorite()
                .withLatitude(-29.796493)
                .withLongitude(-51.148570)
                .build();

        Place pCorredor = new PlaceBuilder()
                .withName("Corredor")
                .withMessage("Continue seguindo reto até a sala de reunião")
                .setDestination()
                .withLatitude(-29.796648)
                .withLongitude(-51.148182)
                .build();

        Place pSalaDeReunião = new PlaceBuilder()
                .withName("Sala de reunião")
                .withMessage("Mensagem de teste!")
                .setDestination()
                .setFavorite()
                .withLatitude(-29.796634)
                .withLongitude(-51.147999)
                .build();

        pRecepcao.setId(mPlaceProvider.insert(pRecepcao));
        pCorredor.setId(mPlaceProvider.insert(pCorredor));
        pSalaDeReunião.setId(mPlaceProvider.insert(pSalaDeReunião));

        /*
         *  Beacon
        */
        Beacon beaconFarol = new Beacon();
        Beacon mIPodBeacon1 = new Beacon();
        Beacon mIPodBeacon2 = new Beacon();

        beaconFarol.setUUID(BeaconProvider.FAROL_BEACON);
        mIPodBeacon1.setUUID(BeaconProvider.MY_BEACON_UUID1);
        mIPodBeacon2.setUUID(BeaconProvider.MY_BEACON_UUID2);

        beaconFarol.setId(mBeaconProvider.insert(beaconFarol));
        mIPodBeacon1.setId(mBeaconProvider.insert(mIPodBeacon1));
        mIPodBeacon2.setId(mBeaconProvider.insert(mIPodBeacon2));

        /*
         *  PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(pRecepcao.getId(), beaconFarol.getId(), beaconFarol.getUUID());
        PlaceBeacon pb2 = new PlaceBeacon(pCorredor.getId(), mIPodBeacon1.getId(), mIPodBeacon1.getUUID());
        PlaceBeacon pb3 = new PlaceBeacon(pSalaDeReunião.getId(), mIPodBeacon2.getId(), mIPodBeacon2.getUUID());

        pb1.setId(mPlaceBeaconProvider.insert(pb1));
        pb2.setId(mPlaceBeaconProvider.insert(pb2));
        pb3.setId(mPlaceBeaconProvider.insert(pb3));


        /*
         *  Path
         */
        Path path1 = new Path(pRecepcao.getId(), pCorredor.getId(), 1);     // 10   20
        Path path2 = new Path(pCorredor.getId(), pSalaDeReunião.getId(), 1);

        path1.setId(mPathProvider.insert(path1));
        path2.setId(mPathProvider.insert(path2));
    }
}
