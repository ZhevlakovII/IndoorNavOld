using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARSubsystems;
using UnityEngine.XR.ARFoundation;
using ZXing;
using Unity.Collections;
using UnityEngine.UI;
using TMPro;

public class QRScanner : MonoBehaviour {

    [SerializeField]
    private ARSession session;
    [SerializeField]
    private ARSessionOrigin sessionOrigin;
    [SerializeField]
    private ARCameraManager cameraManager;
    [SerializeField]
    private List<Target> listOfTargets = new List<Target>();

    private Texture2D cameraImage;
    private IBarcodeReader reader = new BarcodeReader();

    [SerializeField] private TMP_Dropdown dropdownMenu;
    [SerializeField] private Button button;
    [SerializeField] private TMP_Text text;

    private void Start() {
        button.gameObject.SetActive(false);
        dropdownMenu.gameObject.SetActive(false);
        text.gameObject.SetActive(true);
    }

    // Update is called once per frame
    void Update() {
        if (Input.GetKeyDown(KeyCode.Space)) {
            SetQRCodeTarget("Bedroom Y");
        }
    }

    private void OnEnable() {
        cameraManager.frameReceived += OnCameraFrameRecieved;
    }

    private void OnDisable() {
        cameraManager.frameReceived -= OnCameraFrameRecieved;
    }

    private void OnCameraFrameRecieved(ARCameraFrameEventArgs args) {
        if (!cameraManager.TryAcquireLatestCpuImage(out XRCpuImage image)) {
            return;
        }

        var conversionParams = new XRCpuImage.ConversionParams {
            // Get the entire image.
            inputRect = new RectInt(0, 0, image.width, image.height),

            // Downsample by 2.
            outputDimensions = new Vector2Int(image.width / 2, image.height / 2),

            // Choose RGBA format.
            outputFormat = TextureFormat.RGBA32,

            // Flip across the vertical axis (mirror image).
            transformation = XRCpuImage.Transformation.MirrorY
        };

        // See how many bytes you need to store the final image.
        int size = image.GetConvertedDataSize(conversionParams);

        // Allocate a buffer to store the image.
        var buffer = new NativeArray<byte>(size, Allocator.Temp);

        // Extract the image data
        image.Convert(conversionParams, buffer);

        // The image was converted to RGBA32 format and written into the provided buffer
        // so you can dispose of the XRCpuImage. You must do this or it will leak resources.
        image.Dispose();

        // At this point, you can process the image, pass it to a computer vision algorithm, etc.
        // In this example, you apply it to a texture to visualize it.

        // You've got the data; let's put it into a texture so you can visualize it.
        cameraImage = new Texture2D(
            conversionParams.outputDimensions.x,
            conversionParams.outputDimensions.y,
            conversionParams.outputFormat,
            false);

        cameraImage.LoadRawTextureData(buffer);
        cameraImage.Apply();

        // Done with your temporary data, so you can dispose it.
        buffer.Dispose();

        var result = reader.Decode(cameraImage.GetPixels32(), cameraImage.width, cameraImage.height);

        if (result != null) {
            SetQRCodeTarget(result.Text);
        }
    }

    private void SetQRCodeTarget(string target) {
        Target currentTarget = listOfTargets.Find(x => x.Name.ToLower().Equals(target.ToLower()));

        if (currentTarget != null) {
            session.Reset();

            sessionOrigin.transform.position = currentTarget.navObject.transform.position;
            sessionOrigin.transform.rotation = currentTarget.navObject.transform.rotation;

            button.gameObject.SetActive(true);
            dropdownMenu.gameObject.SetActive(true);
            text.gameObject.SetActive(false);
        }
    }
}
