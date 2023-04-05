package michittio.ueh.trifarm_app.fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import michittio.ueh.trifarm_app.ExpandableGridView;
import michittio.ueh.trifarm_app.R;
import michittio.ueh.trifarm_app.SliderAdapter;
import michittio.ueh.trifarm_app.SliderItem;
import michittio.ueh.trifarm_app.data.Product;
import michittio.ueh.trifarm_app.data.ProductAdapter;
import michittio.ueh.trifarm_app.srceen.Detail;
import michittio.ueh.trifarm_app.srceen.ProductDetail;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context thiscontext;


    public HomeFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    private TextView txtSearch;
    private GridView gridView;
    private ArrayList<Product> productArrayList;
    private ProductAdapter adapter;
    final private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Products");



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        viewPager2 = getView().findViewById(R.id.slider);
        List<SliderItem> sliderItems = getListSlider();

//        viewPager2.setCurrentItem(2);
        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });


        gridView = getView().findViewById(R.id.gridView);
        txtSearch= getView().findViewById(R.id.txt_searchButton);
        productArrayList = new ArrayList<>();


        ExpandableGridView productGrid = (ExpandableGridView) getView().findViewById(R.id.gridView);
        productGrid.setExpanded(true);



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        thiscontext = container.getContext();

        txtSearch = rootView.findViewById(R.id.txt_searchButton);
        gridView = rootView.findViewById(R.id.gridView);
        productArrayList = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    productArrayList.add(product);
                }
                adapter = new ProductAdapter(thiscontext, productArrayList);
                gridView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to read value.", error.toException());
            }
        });


        //search data
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thiscontext,CartFragment.class);
                thiscontext.startActivity(intent);
            }
        });


//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Lấy sản phẩm được chọn
//                Product selectedProduct = productArrayList.get(position);
//
//                // Tạo Intent và truyền dữ liệu cho trang Detail
//                Intent intent = new Intent(getActivity(), ProductDetail.class);
//                intent.putExtra("product_name", selectedProduct.getName());
//                intent.putExtra("product_description", selectedProduct.getDescription());
//                intent.putExtra("product_price", selectedProduct.getPrice());
//
//                // Khởi chạy Intent để chuyển sang trang Detail
//                startActivity(intent);
//            }
//        });






        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);



    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000); // Slide duration 3 seconds
    }

    private List<SliderItem> getListSlider () {
        List<SliderItem> sliderItems = new ArrayList<>();

        sliderItems.add(new SliderItem(R.drawable.banner1));
        sliderItems.add(new SliderItem(R.drawable.banner2));
        sliderItems.add(new SliderItem(R.drawable.banner3));
        sliderItems.add(new SliderItem(R.drawable.banner4));

        return sliderItems;
    }
}