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
public class FirstScenarioRecords extends AbstractRecords {

    @Override
    public void insertRecords(Context context) {
        PlaceProvider mPlaceProvider = new PlaceProvider(context);
        BeaconProvider mBeaconProvider = new BeaconProvider(context);
        PlaceBeaconProvider mPlaceBeaconProvider = new PlaceBeaconProvider(context);
        PathProvider mPathProvider = new PathProvider(context);
/*
         *  Places
         */
        Place pEntrada = new PlaceBuilder()
                .withName("Entrada")
                .withDescription("Porta de entrada")
                .withMessage("Ande a frente até passar pela porta automática")
                .withLatitude(-29.796493)
                .withLongitude(-51.148570).build();

        Place pRecepcao = new PlaceBuilder()
                .withName("Recepção")
                .withDescription("Saguão de recepção")
                .withMessage("Dê dois passos a frente e vire a direita, siga até passar pelas catracas")
                .setFavorite()
                .setDestination()
                .withLatitude(-29.796493)
                .withLongitude(-51.148570)
                .build();

        Place pPrimeiroAndar = new PlaceBuilder()
                .withName("Primeiro Andar")
                .withDescription("Acesso aos outros andares")
                .withMessage("Vire a esquerda e ande até chegar a escada, e então suba. São dois lances com 10 degraus")
                .withLatitude(-29.796577)
                .withLongitude(-51.148575)
                .build();

        Place pSegundoAndar = new PlaceBuilder()
                .withName("Segundo Andar")
                .withDescription("Acesso aos outros andares")
                .withMessage("Mensagem...")
                .withLatitude(-29.796577)
                .withLongitude(-51.148575)
                .build();

        Place pEntradaSetor1B = new PlaceBuilder()
                .withName("Entrada do Setor 1B")
                .withMessage("Mensagem de teste!")
                .withLatitude(-29.796493)
                .withLongitude(-51.148570)
                .build();

        Place pCorretor1B = new PlaceBuilder()
                .withName("Corredor Setor 1B")
                .withMessage("Mensagem de teste!")
                .withLatitude(-29.796648)
                .withLongitude(-51.148182)
                .build();

        Place pMesaEduardo = new PlaceBuilder()
                .withName("Posto de trabalho")
                .withMessage("Mensagem de teste!")
                .setFavorite()
                .setDestination()
                .withLatitude(-29.796634)
                .withLongitude(-51.147999)
                .build();

        pEntrada.setId(mPlaceProvider.insert(pEntrada));
        pRecepcao.setId(mPlaceProvider.insert(pRecepcao));
        pPrimeiroAndar.setId(mPlaceProvider.insert(pPrimeiroAndar));
        pSegundoAndar.setId(mPlaceProvider.insert(pSegundoAndar));
        pEntradaSetor1B.setId(mPlaceProvider.insert(pEntradaSetor1B));
        pCorretor1B.setId(mPlaceProvider.insert(pCorretor1B));
        pMesaEduardo.setId(mPlaceProvider.insert(pMesaEduardo));

        /*
         *  Beacon
         */
        Beacon bBranco1 = new Beacon();
        Beacon bAmarelo = new Beacon();
        Beacon bAzul = new Beacon();
        Beacon bPreto = new Beacon();
        Beacon bBranco2 = new Beacon();
        Beacon b6 = new Beacon();
        Beacon novo = new Beacon();

        b6.setUUID(BeaconProvider.MY_BEACON_UUID1);
        bBranco1.setUUID(BeaconProvider.MY_BEACON_UUID2);
        bAmarelo.setUUID(BeaconProvider.MY_BEACON_UUID3);
        bAzul.setUUID(BeaconProvider.MY_BEACON_UUID4);
        bPreto.setUUID(BeaconProvider.MY_BEACON_UUID5);
        bBranco2.setUUID(BeaconProvider.MY_BEACON_UUID6);
        novo.setUUID(BeaconProvider.MY_BEACON_UUID7);


        bBranco1.setId(mBeaconProvider.insert(bBranco1));
        bAmarelo.setId(mBeaconProvider.insert(bAmarelo));
        bAzul.setId(mBeaconProvider.insert(bAzul));
        bPreto.setId(mBeaconProvider.insert(bPreto));
        bBranco2.setId(mBeaconProvider.insert(bBranco2));
        b6.setId(mBeaconProvider.insert(b6));
        novo.setId(mBeaconProvider.insert(novo));


        /*
         *  PlaceBeacon
         */
        PlaceBeacon pb1 = new PlaceBeacon(pRecepcao.getId(), bBranco1.getId(), bBranco1.getUUID());
        PlaceBeacon pb2 = new PlaceBeacon(pPrimeiroAndar.getId(), bAmarelo.getId(), bAmarelo.getUUID());
        PlaceBeacon pb3 = new PlaceBeacon(pSegundoAndar.getId(), bAzul.getId(), bAzul.getUUID());
        PlaceBeacon pb4 = new PlaceBeacon(pEntradaSetor1B.getId(), bPreto.getId(), bPreto.getUUID());
        PlaceBeacon pb5 = new PlaceBeacon(pCorretor1B.getId(), bBranco2.getId(), bBranco2.getUUID());
        PlaceBeacon pb6 = new PlaceBeacon(pEntrada.getId(), b6.getId(), b6.getUUID());
        PlaceBeacon pb7 = new PlaceBeacon(pMesaEduardo.getId(), novo.getId(), novo.getUUID());

        pb1.setId(mPlaceBeaconProvider.insert(pb1));
        pb2.setId(mPlaceBeaconProvider.insert(pb2));
        pb3.setId(mPlaceBeaconProvider.insert(pb3));
        pb4.setId(mPlaceBeaconProvider.insert(pb4));
        pb5.setId(mPlaceBeaconProvider.insert(pb5));
        pb6.setId(mPlaceBeaconProvider.insert(pb6));
        pb7.setId(mPlaceBeaconProvider.insert(pb7));


        /*
         *  Path
         */
        Path ph0 = new Path(pEntrada.getId(), pRecepcao.getId(), 1);     // 10   20
        Path ph1 = new Path(pRecepcao.getId(), pPrimeiroAndar.getId(), 1);     // 10   20
        Path ph3 = new Path(pPrimeiroAndar.getId(), pSegundoAndar.getId(), 1); // 10   30
        Path ph2 = new Path(pSegundoAndar.getId(), pCorretor1B.getId(), 1);         // 20   40
        Path ph4 = new Path(pCorretor1B.getId(), pMesaEduardo.getId(), 1);
        Path ph5 = new Path(pPrimeiroAndar.getId(), pSegundoAndar.getId(), 1);


        ph0.setId(mPathProvider.insert(ph0));
        ph1.setId(mPathProvider.insert(ph1));
        ph2.setId(mPathProvider.insert(ph2));
        ph3.setId(mPathProvider.insert(ph3));
        ph4.setId(mPathProvider.insert(ph4));
        ph5.setId(mPathProvider.insert(ph5));
    }
}
