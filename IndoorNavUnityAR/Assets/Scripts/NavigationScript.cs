using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.AI;
using TMPro;
using System.Linq;

public class NavigationScript : MonoBehaviour {
    [SerializeField] private TMP_Dropdown dropdownMenu;
    [SerializeField] private Button button;

    [SerializeField] private GameObject user;
    private string selectedItemString;
    [SerializeField] private List<Target> listOfTargets = new List<Target>();

    private Transform startPoint, endPoint = null;
    [SerializeField] private LineRenderer pathLine;
    [SerializeField] private GameObject destintationPin;

    private NavMeshPath path;
    private NavMeshHit meshHit;

    private float pathOffsetY = 0.2f;
    private float elapsed, lineWidth = 0.0f;
    private List<Vector3> corners = new List<Vector3>();

    private void Start() {
        button.onClick.AddListener(() => { OnClickButton(); });

        path = new NavMeshPath();

        lineWidth = 0.28f;
        
        pathLine.startWidth = lineWidth;
        pathLine.endWidth = lineWidth;
    }

    private void Update() {
        if (startPoint != null && endPoint != null) {
            pathLine.enabled = true;

            elapsed += Time.deltaTime;
            if (elapsed > 0.3f) {
                elapsed -= 0.3f;

                if (NavMesh.SamplePosition(startPoint.position, out meshHit, 10.0f, NavMesh.AllAreas)) {
                    NavMesh.CalculatePath(meshHit.position, endPoint.position, NavMesh.AllAreas, path);
                }
            }

            pathLine.positionCount = path.corners.Length;

            corners.Clear();

            foreach (Vector3 corner in path.corners) {
                corners.Add(corner + Vector3.up * pathOffsetY);
            }

            pathLine.SetPositions(corners.ToArray());

            if (corners.Count != 0) {
                var lastLinePoint = corners.Last();
                destintationPin.transform.position = new Vector3(lastLinePoint.x, lastLinePoint.y + 0.3f, lastLinePoint.z);
                destintationPin.SetActive(true);
            }

            var target = Camera.main.transform;
            destintationPin.transform.LookAt(target);

            Collider[] hits = Physics.OverlapSphere(endPoint.transform.position, 0.3f);
            for (int i = 0; i < hits.Length; i++) {
                if (hits[i].transform.position == transform.position) {
                    pathLine.enabled = false;
                    pathLine.positionCount = 0;
                    endPoint = null;
                    destintationPin.SetActive(false);
                }
            }
        } else {
            pathLine.enabled = false;
            pathLine.positionCount = 0;
            destintationPin.SetActive(false);
        }
    }

    public void OnClickButton() {
        selectedItemString = dropdownMenu.captionText.text;
        Target currentTarget = listOfTargets.Find(x => x.Name.ToLower().Equals(selectedItemString.ToLower()));

        startPoint = transform;
        endPoint = currentTarget.navObject.transform;
    }
}
