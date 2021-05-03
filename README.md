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
- Convert to DataFrame.
- Convert to LabeledPoint.
- Split as training/test data into our model.

## Data smoothing
- Filter invalid entries
    - Strings, Nulls
- Convert string(double) to double
- Remove duplicate rows
- Normalize
- ElementwiseProduct
