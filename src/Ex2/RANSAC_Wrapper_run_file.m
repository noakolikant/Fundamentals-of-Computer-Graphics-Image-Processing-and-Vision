
fittingfn = @homfitfn;
distfn = @homdistfn;
%degenfn = @isdegenerate;
degenfn = @(x) 0;
t = 3;
s = 4; 
feedback = 0;
maxDataTrials = 100;
maxTrials = 1000;

H_ransac = RANSAC_Wrapper(matches, fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials)
new_im_ransac = ComputeProjective(Im, H_ransac); imshow(new_im_ransac)