package com.example.cristina.tfgapp.charts.piecharts;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.cristina.tfgapp.model.Product;
import com.example.cristina.tfgapp.R;
import com.example.cristina.tfgapp.controller_view.MainActivity;
import com.example.cristina.tfgapp.controller_view.MyTerminal;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Cristina on 02/06/17.
 */

public class PieChart extends MyTerminal {
    /*
    Activity que contiene la gráfica de tarta que representa el número de unidades vendidas de cada producto
    */

    CategorySeries categorySeries;
    ArrayList<Product> arrayListProducts;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        arrayListProducts = new ArrayList<Product>(){};
        ArrayList<Product> arrayListAux = MainActivity.prodDataBaseHelper.selectAll();
        /*arrayListProducts es un array list de Product donde se almacenarán aquellos productos que han sido comprados al menos una vez,
        es decir, aquellos productos cuyo atributo quantity sea mayor que 0.
        Para ello se utiliza un array list auxiliar, arrayListAux, en el que se vuelca toda la base de datos de productos */
        for (Product product : arrayListAux){
            if (product.getQuantity()>0) {
                arrayListProducts.add(product);
            }
        }
        if (arrayListProducts.size()!=0) drawChart();
        //si no hubiera datos a representar se mostraría un layout que lo indica.
        else setContentView(R.layout.nodata_layout);
    }

    /*
    Función que dibuja el gráfico
     */
    public void drawChart(){
        DefaultRenderer renderer = buildCategoryRenderer();
        renderer.setShowLabels(false);
        renderer.setTextTypeface(Typeface.create("sans-serif-smallcaps",Typeface.NORMAL));
        renderer.setLegendHeight(40);
        renderer.setLabelsColor(Color.BLACK);
        renderer.setLegendTextSize(22);
        renderer.setPanEnabled(false);
        renderer.setZoomEnabled(false);

        //Legend
        renderer.setFitLegend(true);
        renderer.setAntialiasing(true);
        categorySeries = new CategorySeries(getString(R.string.categorySeriesTitlePieChart));
        double totalAmount = 0.0;
        for (int i=0; i<arrayListProducts.size(); i++) {
            totalAmount += arrayListProducts.get(i).getQuantity();
        }
        for (int i=0; i<arrayListProducts.size(); i++) {
            //Porcentaje que representa el número de unidades vendidas de ese producto sobre el total.
            double percentage = arrayListProducts.get(i).getQuantity()*100/totalAmount;
            //Se redondea a dos decimales el porcentaje
            percentage = Math.round(percentage*100.0)/100.0;
            //Formato: Nombre del producto (Núm unidades vendidas) (Porcentaje que representa)
            String cat = arrayListProducts.get(i).getName()+ "  ("+String.valueOf(arrayListProducts.get(i).getQuantity())+") "+"("+String.valueOf(percentage)+"%) ";
            int numberSales = arrayListProducts.get(i).getQuantity();
            categorySeries.add(cat, numberSales);
        }
        renderer.setDisplayValues(false);
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);
        chartContainer.removeAllViews();
        View mChart = ChartFactory.getPieChartView(getBaseContext(), categorySeries, renderer);
        chartContainer.addView(mChart);
    }

    /*
    Función que genera el SimpleSeriesRenderer y le añade las diferentes series, es decir, los diferentes nombres de producto y su color asociado.
    Estos colores se generan aleatoriamente, ya que arrayListProducts crece de forma dinámica y no se puede saber de antemano el número
    de productos a representar que habrá
     */
    protected DefaultRenderer buildCategoryRenderer() {
        //Los colores se generan aleatoriamente en tiempo de ejecución porque de primeras no se sabe cuántos productos han sido vendidos,no se sabe si va a haber 3 o 10 productos diferentes en el gráfico
        DefaultRenderer renderer = new DefaultRenderer();
        for (Product product : arrayListProducts) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            Random rand = new Random();
            int red = rand.nextInt(255);
            int g = rand.nextInt(255);
            int b = rand.nextInt(255);
            int randomColor = Color.rgb(red,g,b);
            r.setColor(randomColor);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

}
