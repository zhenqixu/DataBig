# DataBig

## Installation and Running 
## Model 
- LogisticRegressionWithLBFGS
    - Is able to handle multiple labels but we only have two right now.
## Noises
- Flip the labels
    - The label of 1 is changed to label of 0.
- Assign some of the entry value of string type
    - 3.14159 becomes "3.14159"
- Duplicate rows: 
    - Two rows with exact same values
- Missing entries:
    - Some of the entry values are missing and assigned to value 'Null'
- Invalid entries:
    - Some of the entry values are invalid and they are assigned to 'infinity' or some alphabets.
- Different noises for each dimesnion
```
    if (noise):
        v1 += random.uniform(0, 0.03)
        v2 += random.uniform(-0.05, 0)
        v3 += random.uniform(-0.05, 0.05)
        v4 += random.uniform(0.1, 0.12)
```
## Data Process
- Import data files as JSON format.
- Join multiple data files as one.
- Remove the redundant data

- Convert Dataframe to RDD, and convert the entry to double type
    - Remove invaild entry  
        - Convert all the entries to string type
        - Filter non-numeric entries and convert the rest to Double type
        - Filter rows with invalid size

- Convert to LabeledPoint
- Scale and Normalize the Dataset
- Split as training/test data into our model. 80% vs. 20%
- Get results

## Data smoothing
- Replace null values with AVG of that column
- Scale DataSet
- Normalize DataSet
- Elementwise Product
