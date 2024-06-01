package com.amaurypm.ireaderqr

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.amaurypm.ireaderqr.databinding.FragmentMainBinding


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    //Para el permiso de la cámara
    private var cameraPermissionGranted = false

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            //Se concedió el permiso
            actionPermissionGranted()
        }else{
            if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                AlertDialog.Builder(requireContext())
                    .setTitle("Permiso requerido")
                    .setMessage("Se requiere el permiso de la cámara solamente para leer los códigos")
                    .setPositiveButton("Aceptar"){ _, _  ->
                        updateOrRequestPermissions()
                    }
                    .setNegativeButton("Salir"){ dialog, _ ->
                        dialog.dismiss()
                        requireActivity().finish()
                    }
                    .create()
                    .show()
            }else{
                //Lo negó permanentemente
                Toast.makeText(
                    requireContext(),
                    "El permiso a la cámara se ha negado permanentemente",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivCamara.setOnClickListener {
            updateOrRequestPermissions()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun updateOrRequestPermissions(){

        cameraPermissionGranted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if(!cameraPermissionGranted){
            //Pedimos el permiso
            permissionsLauncher.launch(Manifest.permission.CAMERA)
        }else{
            //Tenemos el permiso
            actionPermissionGranted()
        }

    }

    private fun actionPermissionGranted(){
        findNavController().navigate(R.id.action_mainFragment_to_scannerFragment)
    }

}