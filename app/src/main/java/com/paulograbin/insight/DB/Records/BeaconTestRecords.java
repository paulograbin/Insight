package com.paulograbin.insight.DB.Records;

import android.content.Context;

import com.paulograbin.insight.DB.Provider.BeaconProvider;
import com.paulograbin.insight.DB.Provider.PathProvider;
import com.paulograbin.insight.DB.Provider.PlaceBeaconProvider;
import com.paulograbin.insight.DB.Provider.PlaceProvider;
import com.paulograbin.insight.Model.Beacon;
import com.paulograbin.insight.Model.Place;
import com.paulograbin.insight.Model.PlaceBeacon;

/**
 * Created by paulograbin on 02/12/15.
 */
public class BeaconTestRecords extends AbstractRecords {

    @Override
    public void insertRecords(Context context) {
        PlaceProvider mPlaceProvider = new PlaceProvider(context);
        BeaconProvider mBeaconProvider = new BeaconProvider(context);
        PlaceBeaconProvider mPlaceBeaconProvider = new PlaceBeaconProvider(context);
        PathProvider mPathProvider = new PathProvider(context);

        /**
         *  PLACES
         */
        Place pAmarelo    = new Place("Amarelo", "Novo Hamburgo",    "", 0, 1, -29.685204, -51.140426);
        Place pAzul       = new Place("Azul", "São Leopoldo",        "", 0, 1, -29.772558, -51.151413);
        Place pPreto      = new Place("Preto", "Esteio",             "", 0, 1, -29.848991, -51.175000);
        Place pBrancoUm   = new Place("Branco Um", "Canoas",         "", 0, 1, -29.913290, -51.179120);
        Place pBrancoDois = new Place("Branco Dois", "Porto Alegre", "", 0, 1, -30.037008, -51.227528);

        pAmarelo.setId(mPlaceProvider.insert(pAmarelo));
        pAzul.setId(mPlaceProvider.insert(pAzul));
        pPreto.setId(mPlaceProvider.insert(pPreto));
        pBrancoUm.setId(mPlaceProvider.insert(pBrancoUm));
        pBrancoDois.setId(mPlaceProvider.insert(pBrancoDois));


        /**
         *  BEACONS
         */
        Beacon bAzul = new Beacon();
        bAzul.setUUID(BeaconProvider.BEACON_AZUL);

        Beacon bAmarelo = new Beacon();
        bAmarelo.setUUID(BeaconProvider.BEACON_AMARELO);

        Beacon mBeaconProviderreto = new Beacon();
        mBeaconProviderreto.setUUID(BeaconProvider.BEACON_PRETO);

        Beacon bBrancoUm = new Beacon();
        bBrancoUm.setUUID(BeaconProvider.BEACON_BRANCO_1);

        Beacon bBrancoDois =  new Beacon();
        bBrancoDois.setUUID(BeaconProvider.BEACON_BRANCO_2);

        bAzul.setId(mBeaconProvider.insert(bAzul));
        bAmarelo.setId(mBeaconProvider.insert(bAmarelo));
        mBeaconProviderreto.setId(mBeaconProvider.insert(mBeaconProviderreto));
        bBrancoUm.setId(mBeaconProvider.insert(bBrancoUm));
        bBrancoDois.setId(mBeaconProvider.insert(bBrancoDois));


        /**
         *  PLACEBEACONS
         */
        PlaceBeacon mPlaceBeaconProviderreto = new PlaceBeacon(pPreto.getId(), mBeaconProviderreto.getId(), mBeaconProviderreto.getUUID());
        mPlaceBeaconProviderreto.setId(mPlaceBeaconProvider.insert(mPlaceBeaconProviderreto));

        PlaceBeacon pbAmarelo = new PlaceBeacon(pAmarelo.getId(), bAmarelo.getId(), bAmarelo.getUUID());
        pbAmarelo.setId(mPlaceBeaconProvider.insert(pbAmarelo));

        PlaceBeacon pbAzul = new PlaceBeacon(pAzul.getId(), bAzul.getId(), bAzul.getUUID());
        pbAzul.setId(mPlaceBeaconProvider.insert(pbAzul));

        PlaceBeacon pbBrancoUm = new PlaceBeacon(pBrancoUm.getId(), bBrancoUm.getId(), bBrancoUm.getUUID());
        pbBrancoUm.setId(mPlaceBeaconProvider.insert(pbBrancoUm));

        PlaceBeacon pbBrancoDois = new PlaceBeacon(pBrancoDois.getId(), bBrancoDois.getId(), bBrancoDois.getUUID());
        pbBrancoDois.setId(mPlaceBeaconProvider.insert(pbBrancoDois));
    }
}
