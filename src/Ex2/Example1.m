
% image1_file = <!--PATH_TO_IMAGE-->;
% image2_file = <!--PATH_TO_IMAGE-->;
% image3_file = <!--PATH_TO_IMAGE-->;
image1_file = 'C:\Users\Ophir\workspace\RayTracer\Fundamentals-of-Computer-Graphics-Image-Processing-and-Vision\src\Ex2\lena.bmp';
image2_file = 'C:\Users\Ophir\workspace\RayTracer\Fundamentals-of-Computer-Graphics-Image-Processing-and-Vision\src\Ex2\lena2.bmp';
image3_file = 'C:\Users\Ophir\workspace\RayTracer\Fundamentals-of-Computer-Graphics-Image-Processing-and-Vision\src\Ex2\lena3.bmp';
Im = imread(image1_file, 'bmp');
H_gt = [1 0.2 0; 0.1 1 0; 0.5 0.2 1];
new_im = ComputeProjective(Im, H_gt);
imwrite(new_im, image2_file, 'bmp');
[ num_matches,matches,dist_vals ] = match(image1_file, image2_file, 0.6);
H_comp = DLT(matches); 
new_im_comp = ComputeProjective(Im, H_comp);
imwrite(new_im_comp, image3_file, 'bmp')

random_matches = matches;
% Add some noise to matches:

for i=size(matches,1):size(matches,1)+200
    random_matches(i, :) =  randi([1, 512], 1, 4);
end
fittingfn = @homfitfn;
distfn = @homdistfn;
degenfn = @isdegenerate;
t = 30;
s = 4; 
feedback = 0;
maxDataTrials = 100;
maxTrials = 10000;

H_ransac = RANSAC_Wrapper(random_matches, fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials);
[ pnts_gt, pnts_computed ] = ComputeTestPoints(H_gt, H_ransac);
error_ransac = ComputeError(pnts_gt, pnts_computed) / 100

H_comp = DLT(random_matches); new_im_comp = ComputeProjective(Im, H_comp); imshow(new_im_comp)
[ pnts_gt, pnts_computed ] = ComputeTestPoints(H_gt, H_comp);
error_dlt = ComputeError(pnts_gt, pnts_computed) / 100
    
new_im_ransac = ComputeProjective(Im, H_ransac); imshow(new_im_ransac)